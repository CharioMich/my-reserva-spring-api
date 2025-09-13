package gr.aueb.cf.myreserva.api;

import gr.aueb.cf.myreserva.authentication.AuthenticationService;
import gr.aueb.cf.myreserva.core.exceptions.AppObjectAlreadyExists;
import gr.aueb.cf.myreserva.core.exceptions.AppObjectInvalidArgumentException;
import gr.aueb.cf.myreserva.core.exceptions.AppObjectNotAuthorizedException;
import gr.aueb.cf.myreserva.core.exceptions.ValidationException;
import gr.aueb.cf.myreserva.dto.*;
import gr.aueb.cf.myreserva.dto.user.UserInsertDTO;
import gr.aueb.cf.myreserva.dto.user.UserReadOnlyDTO;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Null;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthRestController {

    private static final Logger LOGGER = LoggerFactory.getLogger(AuthRestController.class);
    private final AuthenticationService authenticationService;

    @Value("${jwt.refresh-expiration-ms}")
    private long jwtRefreshExpiration;


    /**
     * LOGIN
     */
    @PostMapping("/login")
    public ResponseEntity<ApiAuthResponse> authenticate(
            @Valid @ModelAttribute AuthenticationRequestDTO authenticationRequestDTO, // @ModelAttribute for x-form-urlencoded data
            HttpServletResponse response
    )
            throws AppObjectNotAuthorizedException {

        TokensAndUserDTO tokensAndUserDTO = authenticationService.authenticate(authenticationRequestDTO);
        String refreshToken = tokensAndUserDTO.refreshToken();

        // Set HTTP-only cookie
        ResponseCookie cookie = ResponseCookie.from("refreshToken", refreshToken)
                .httpOnly(true)
                .secure(false)   // use true in production environment with HTTPS
                .path("/")
                .maxAge(jwtRefreshExpiration)
                .sameSite("Strict")
                .build();
        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());

        AuthenticationResponseDTO authenticationResponseDTO = new AuthenticationResponseDTO(tokensAndUserDTO.user(), tokensAndUserDTO.accessToken());
        ApiAuthResponse apiResponse = new ApiAuthResponse(
                true,
                "User logged in",
                authenticationResponseDTO.user(),
                authenticationResponseDTO.accessToken()
        );

        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }


    /**
     * REGISTER
     */
    @PostMapping("/register")
    public ResponseEntity<ApiAuthResponse> register(
            @Valid @ModelAttribute UserInsertDTO insertDTO,
            BindingResult bindingResult
    ) throws AppObjectAlreadyExists, ValidationException {
        try {
            if (bindingResult.hasErrors()) {
                bindingResult.getAllErrors().forEach(err ->
                        LOGGER.error("Validation error: {}", err.getDefaultMessage())
                );
                throw new ValidationException(bindingResult);
            }

            AuthenticationResponseDTO authenticationResponseDTO = authenticationService.register(insertDTO);
            UserReadOnlyDTO userReadOnlyDTO = authenticationResponseDTO.user();
            String accessToken = authenticationResponseDTO.accessToken();

            ApiAuthResponse apiResponse = new ApiAuthResponse(
                    true,
                    "New user created",
                    userReadOnlyDTO,
                    accessToken
            );

            return new ResponseEntity<>(apiResponse, HttpStatus.CREATED);
        } catch (Exception e) {
            LOGGER.warn("Failed to register new user", e);
            throw e;
        }
    }


    /**
     * REFRESH TOKEN
     */
    @PostMapping("/refresh-token")
    public ResponseEntity<AuthenticationResponseDTO> refreshToken(
            @CookieValue(name = "refreshToken", required = false) String refreshToken,
                                                                  HttpServletResponse response) throws AppObjectInvalidArgumentException {
        if (refreshToken == null) throw new AppObjectInvalidArgumentException("Refresh Token", "Refresh token not provided");

        try {
            TokensAndUserDTO tokensAndUserDTO = authenticationService.verifyRefreshToken(refreshToken);
            String newRefreshToken = tokensAndUserDTO.refreshToken();

            // Set HTTP-only cookie
            ResponseCookie cookie = ResponseCookie.from("refreshToken", newRefreshToken)
                    .httpOnly(true)
                    .secure(false)   // use true in production environment with HTTPS
                    .path("/")
                    .maxAge(jwtRefreshExpiration)
                    .sameSite("Strict")
                    .build();
            response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());

            AuthenticationResponseDTO authenticationResponseDTO = new AuthenticationResponseDTO(tokensAndUserDTO.user(), tokensAndUserDTO.accessToken());
            return new ResponseEntity<>(authenticationResponseDTO, HttpStatus.OK);

        } catch(AppObjectInvalidArgumentException e) {
            LOGGER.error("Refresh token not provided");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch(AppObjectNotAuthorizedException e) {
            LOGGER.error("Refresh token invalid or expired");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }


    /**
     * LOGOUT
     */
    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<Null>> logout(
            @CookieValue(name = "refreshToken", required = false) String refreshToken,
            HttpServletResponse response
    ) throws AppObjectNotAuthorizedException {

        // Try to delete refresh token from db if it exists
        if (refreshToken != null && !refreshToken.isEmpty()) {
            try {
                authenticationService.logout(refreshToken);
            } catch (AppObjectNotAuthorizedException e) {
                LOGGER.info("Refresh token not provided, user already logged out");
            }
        }

        ApiResponse<Null> apiResponse = new ApiResponse<>(
                true,
                "User logged out, token cleared",
                null
        );

        // Clear cookie in any case
        ResponseCookie cookie = ResponseCookie.from("refreshToken", "")
                .httpOnly(true)
                .secure(false)         // set to true in production
                .path("/")
                .maxAge(0)            // deletes the cookie immediately
                .sameSite("Lax")   // CSRF protection
                .build();

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body(apiResponse);
    }

}