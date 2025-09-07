package gr.aueb.cf.myreserva.api;

import gr.aueb.cf.myreserva.authentication.AuthenticationService;
import gr.aueb.cf.myreserva.core.exceptions.AppObjectAlreadyExists;
import gr.aueb.cf.myreserva.core.exceptions.AppObjectNotAuthorizedException;
import gr.aueb.cf.myreserva.dto.AuthenticationRequestDTO;
import gr.aueb.cf.myreserva.dto.AuthenticationResponseDTO;
import gr.aueb.cf.myreserva.dto.UserInsertDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthRestController {

    private static final Logger LOGGER = LoggerFactory.getLogger(AuthRestController.class);

    private final AuthenticationService authenticationService;

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponseDTO> authenticate(@Valid @ModelAttribute AuthenticationRequestDTO authenticationRequestDTO) // @ModelAttribute for x-form-urlencoded data
            throws AppObjectNotAuthorizedException {
        AuthenticationResponseDTO authenticationResponseDTO = authenticationService.authenticate(authenticationRequestDTO);
        return new ResponseEntity<>(authenticationResponseDTO, HttpStatus.OK);
    }


    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponseDTO> register(@Valid @RequestBody UserInsertDTO insertDTO) throws AppObjectAlreadyExists {
        AuthenticationResponseDTO authenticationResponseDTO = authenticationService.register(insertDTO);
        return new ResponseEntity<>(authenticationResponseDTO, HttpStatus.CREATED);
    }

}