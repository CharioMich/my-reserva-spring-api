package gr.aueb.cf.myreserva.mapper;

import gr.aueb.cf.myreserva.dto.*;
import gr.aueb.cf.myreserva.model.Reservation;
import gr.aueb.cf.myreserva.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class Mapper {

    private final PasswordEncoder passwordEncoder;

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

    public ReservationReadOnlyDTO mapToReservationReadOnlyDTO(Reservation reservation) {
        return new ReservationReadOnlyDTO(
                reservation.getId(),
                reservation.getDateTime(),
                reservation.getHours(),
                reservation.getText()
        );
    }

}
