package gr.aueb.cf.myreserva.mapper;

import gr.aueb.cf.myreserva.dto.*;
import gr.aueb.cf.myreserva.model.Reservation;
import gr.aueb.cf.myreserva.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class Mapper {


    public UserReadOnlyDTO mapToUserReadOnlyDTO(User user) {
        return new UserReadOnlyDTO(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getFirstname(),
                user.getLastname(),
                user.getPhoneNumber(),
                user.getRole().toString()
        );
    }

    public AuthenticationResponseDTO mapToAuthenticationResponseDTO(User user, String token) {
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


    public User mapToUserEntity(UserInsertDTO insertDTO) {
        User user = new User();
        user.setUsername(insertDTO.username());
        user.setEmail(insertDTO.email());
        user.setPassword(insertDTO.password());
        user.setFirstname(insertDTO.firstname());
        user.setLastname(insertDTO.lastname());
        user.setPhoneNumber(insertDTO.phoneNumber());
        user.setRole(insertDTO.role());

        return user;
    }


    public ReservationReadOnlyDTO mapToReservationReadOnlyDTO(Reservation reservation) {
        return new ReservationReadOnlyDTO(
                reservation.getId(),
                reservation.getDateTime(),
                reservation.getHours(),
                reservation.getText()
        );
    }

}
