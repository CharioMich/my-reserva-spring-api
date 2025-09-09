package gr.aueb.cf.myreserva.authentication;

import gr.aueb.cf.myreserva.core.exceptions.AppObjectAlreadyExists;
import gr.aueb.cf.myreserva.core.exceptions.AppObjectNotAuthorizedException;
import gr.aueb.cf.myreserva.dto.AuthenticationRequestDTO;
import gr.aueb.cf.myreserva.dto.AuthenticationResponseDTO;
import gr.aueb.cf.myreserva.dto.user.UserInsertDTO;
import gr.aueb.cf.myreserva.mapper.Mapper;
import gr.aueb.cf.myreserva.model.User;
import gr.aueb.cf.myreserva.repository.UserRepository;
import gr.aueb.cf.myreserva.security.JwtService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final AuthenticationManager authenticationManager;
    private final Mapper mapper;
    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;


    public AuthenticationResponseDTO authenticate(AuthenticationRequestDTO dto)
            throws AppObjectNotAuthorizedException {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(dto.email(), dto.password()));

        User user = userRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new AppObjectNotAuthorizedException("User", "User not authorized"));

        String token = jwtService.generateToken(authentication.getName(), user.getRole().name());
        return new AuthenticationResponseDTO(
                user.getUsername(),
                user.getEmail(),
                user.getFirstname(),
                user.getLastname(),
                user.getPhoneNumber(),
                user.getRole().toString(),
                token
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

        String token = jwtService.generateToken(savedUser.getEmail(), savedUser.getRole().name());

        return mapper.mapToAuthenticationResponseDTO(savedUser, token);

    }

}
