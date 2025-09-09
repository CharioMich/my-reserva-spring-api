package gr.aueb.cf.myreserva.api;

import gr.aueb.cf.myreserva.core.exceptions.AppObjectAlreadyExists;
import gr.aueb.cf.myreserva.core.exceptions.AppObjectNotFoundException;
import gr.aueb.cf.myreserva.core.exceptions.ValidationException;
import gr.aueb.cf.myreserva.dto.user.UserInsertDTO;
import gr.aueb.cf.myreserva.dto.user.UserReadOnlyDTO;
import gr.aueb.cf.myreserva.dto.user.UserUpdateDTO;
import gr.aueb.cf.myreserva.mapper.Mapper;
import gr.aueb.cf.myreserva.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;


@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserRestController {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserRestController.class);
    private final UserService userService;
    private final Mapper mapper;

    /**
     * GET CURRENT LOGGED-IN USER
     * @param authentication When controller method has a parameter of type Authentication, Spring automatically resolves it from the current SecurityContext.
     *                                 The Authentication object is created in the JwtAuthenticationFilter
     */
    @GetMapping("/current")
    public ResponseEntity<UserReadOnlyDTO> getCurrentUser(Authentication authentication) throws AppObjectNotFoundException {
        try {
            String email = authentication.getName();    // email extracted from JWT

            UserReadOnlyDTO userReadOnlyDTO = userService.getCurrentUser(email);

            return ResponseEntity.ok(userReadOnlyDTO);
        } catch (AppObjectNotFoundException e) {
            LOGGER.warn("Could not get current user.", e);
            throw e;
        }
    }


    /**
     * UPDATE CURRENT LOGGED-IN USER
     */
    @PutMapping("/current")
    public ResponseEntity<UserReadOnlyDTO> updateCurrentUser(
            @Valid @ModelAttribute UserUpdateDTO updateDTO,
            Authentication authentication,
            BindingResult bindingResult
    )
            throws AppObjectNotFoundException, ValidationException, AppObjectAlreadyExists, IOException {

        String email = authentication.getName();

        try {

            if (bindingResult.hasErrors()) {
                throw new ValidationException(bindingResult);
            }

            UserReadOnlyDTO userReadOnlyDTO = userService.updateUser(updateDTO, email);

            return new ResponseEntity<>(userReadOnlyDTO, HttpStatus.OK);

        } catch (Exception e) {
            LOGGER.warn("User to update, with email:{} not found.", email);
            throw e;
        }
    }
}
