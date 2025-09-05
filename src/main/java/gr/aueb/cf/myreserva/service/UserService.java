package gr.aueb.cf.myreserva.service;

import gr.aueb.cf.myreserva.core.exceptions.AppObjectNotFoundException;
import gr.aueb.cf.myreserva.dto.UserReadOnlyDTO;
import gr.aueb.cf.myreserva.mapper.Mapper;
import gr.aueb.cf.myreserva.model.User;
import gr.aueb.cf.myreserva.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final Mapper mapper;

    public UserReadOnlyDTO getCurrentUser(String email) throws AppObjectNotFoundException {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new AppObjectNotFoundException("User", "User with email " + email + " not found."));
        return mapper.mapToUserReadOnlyDTO(user);
    }
}
