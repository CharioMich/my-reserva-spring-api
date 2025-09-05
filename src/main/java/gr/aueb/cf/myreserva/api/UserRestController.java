package gr.aueb.cf.myreserva.api;

import gr.aueb.cf.myreserva.core.exceptions.AppObjectNotFoundException;
import gr.aueb.cf.myreserva.dto.UserReadOnlyDTO;
import gr.aueb.cf.myreserva.mapper.Mapper;
import gr.aueb.cf.myreserva.service.UserService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserRestController {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserRestController.class);
    private final UserService userService;
    private final Mapper mapper;

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
}
