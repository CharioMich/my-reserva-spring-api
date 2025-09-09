package gr.aueb.cf.myreserva.service;

import gr.aueb.cf.myreserva.core.exceptions.AppObjectAlreadyExists;
import gr.aueb.cf.myreserva.core.exceptions.AppObjectNotFoundException;
import gr.aueb.cf.myreserva.dto.user.UserInsertDTO;
import gr.aueb.cf.myreserva.dto.user.UserReadOnlyDTO;
import gr.aueb.cf.myreserva.dto.user.UserUpdateDTO;
import gr.aueb.cf.myreserva.mapper.Mapper;
import gr.aueb.cf.myreserva.model.User;
import gr.aueb.cf.myreserva.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final Mapper mapper;

    /**
     * GET USER BY EMAIL (Get current User)
     */
    public UserReadOnlyDTO getCurrentUser(String email) throws AppObjectNotFoundException {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new AppObjectNotFoundException("User", "User with email " + email + " not found."));
        return mapper.mapToUserReadOnlyDTO(user);
    }


    /**
     * UPDATE CURRENT USER
     */
    @Transactional(rollbackOn = {AppObjectNotFoundException.class, IOException.class}) // In case of runtime exceptions or specified checked exceptions, rollback.
    public UserReadOnlyDTO updateUser(UserUpdateDTO updateDTO, String email) throws AppObjectNotFoundException, IOException, AppObjectAlreadyExists {

        User existingUser = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User with email " + email + " not found."));

        // Check if email or phone number already exist in some other user's data
        if (userRepository.existsByEmail(updateDTO.email())
                && !existingUser.getEmail().equals(updateDTO.email())) {
            throw new AppObjectAlreadyExists("User", "Email already in use: " + updateDTO.email());
        }
        if (userRepository.existsByPhoneNumber(updateDTO.phoneNumber())
                && !existingUser.getPhoneNumber().equals(updateDTO.phoneNumber())) {
            throw new AppObjectAlreadyExists("User", "Phone number already in use: " + updateDTO.phoneNumber());
        }

        // Update only the necessary fields from DTO if they are provided (not null)
        if (updateDTO.firstname() != null) existingUser.setFirstname(updateDTO.firstname());
        if (updateDTO.lastname() != null) existingUser.setLastname(updateDTO.lastname());
        if (updateDTO.email() != null) existingUser.setEmail(updateDTO.email());
        if (updateDTO.phoneNumber() != null) existingUser.setPhoneNumber(updateDTO.phoneNumber());

        User savedUser = userRepository.save(existingUser);

        return mapper.mapToUserReadOnlyDTO(savedUser);
    }
}
