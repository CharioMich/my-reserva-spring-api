package gr.aueb.cf.myreserva.authentication;

import gr.aueb.cf.myreserva.core.exceptions.AppObjectAlreadyExists;
import gr.aueb.cf.myreserva.core.exceptions.AppObjectInvalidArgumentException;
import gr.aueb.cf.myreserva.core.exceptions.AppObjectNotAuthorizedException;
import gr.aueb.cf.myreserva.dto.AuthenticationRequestDTO;
import gr.aueb.cf.myreserva.dto.AuthenticationResponseDTO;
import gr.aueb.cf.myreserva.dto.TokensAndUserDTO;
import gr.aueb.cf.myreserva.dto.user.UserInsertDTO;
import gr.aueb.cf.myreserva.dto.user.UserReadOnlyDTO;
import gr.aueb.cf.myreserva.mapper.Mapper;
import gr.aueb.cf.myreserva.model.RefreshToken;
import gr.aueb.cf.myreserva.model.User;
import gr.aueb.cf.myreserva.repository.RefreshTokenRepository;
import gr.aueb.cf.myreserva.repository.UserRepository;
import gr.aueb.cf.myreserva.security.JwtService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final AuthenticationManager authenticationManager;
    private final Mapper mapper;
    private final JwtService jwtService;
    private final RefreshTokenRepository refreshTokenRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;


    public TokensAndUserDTO authenticate(AuthenticationRequestDTO dto)
            throws AppObjectNotAuthorizedException {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(dto.email(), dto.password()));

        User user = userRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new AppObjectNotAuthorizedException("User", "User not authorized"));

        String accessToken = jwtService.generateAccessToken(authentication.getName(), user.getRole().name());
        String refreshToken = jwtService.generateRefreshToken(authentication.getName());

        RefreshToken refreshTokenEntity = new RefreshToken();
        refreshTokenEntity.setUser(user);
        refreshTokenEntity.setToken(refreshToken);
        refreshTokenRepository.save(refreshTokenEntity);

        UserReadOnlyDTO userReadOnlyDTO = mapper.mapToUserReadOnlyDTO(user);

        return new TokensAndUserDTO(
                userReadOnlyDTO,
                accessToken,
                refreshToken
        );
    }


    @Transactional(rollbackOn = {AppObjectAlreadyExists.class, IOException.class})
    public AuthenticationResponseDTO register(UserInsertDTO insertDTO) throws AppObjectAlreadyExists {

        if (userRepository.findByEmail(insertDTO.email()).isPresent()) {
            throw new AppObjectAlreadyExists("User", "User with email " + insertDTO.email() + " already exists");
        }

        User user = mapper.mapToUserEntity(insertDTO);

        user.setPassword(passwordEncoder.encode(insertDTO.password()));

        User savedUser = userRepository.save(user);
        UserReadOnlyDTO userReadOnlyDTO = mapper.mapToUserReadOnlyDTO(savedUser);

        String token = jwtService.generateAccessToken(savedUser.getEmail(), savedUser.getRole().name());

        return new AuthenticationResponseDTO(userReadOnlyDTO, token);

    }


    public TokensAndUserDTO verifyRefreshToken(String refreshTokenCookie) throws AppObjectInvalidArgumentException, AppObjectNotAuthorizedException {

        RefreshToken refreshToken = refreshTokenRepository.findByToken(refreshTokenCookie)
                .orElseThrow(() -> new AppObjectInvalidArgumentException("Refresh Token", "Invalid refresh token."));

        User user = refreshToken.getUser();

        if (!jwtService.isTokenValid(refreshTokenCookie, user.getEmail())) {
            refreshTokenRepository.delete(refreshToken);
            throw new AppObjectNotAuthorizedException("Refresh Token", "Refresh token expired or invalid.");
        }
        // Generate new tokens
        String newAccessToken = jwtService.generateAccessToken(user.getUsername(), user.getRole().toString());
        String newRefreshToken = jwtService.generateRefreshToken(user.getUsername());

        // Rotate refresh token in DB
        refreshToken.setToken(newRefreshToken);
        refreshTokenRepository.save(refreshToken);

        UserReadOnlyDTO userReadOnlyDTO = mapper.mapToUserReadOnlyDTO(user);

        return new TokensAndUserDTO(
                userReadOnlyDTO,
                newAccessToken,
                newRefreshToken
        );
    }

}
