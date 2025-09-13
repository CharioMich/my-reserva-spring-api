package gr.aueb.cf.myreserva.mapper;

import gr.aueb.cf.myreserva.core.enums.Role;
import gr.aueb.cf.myreserva.dto.*;
import gr.aueb.cf.myreserva.dto.reservation.ReservationPublicReadOnlyDTO;
import gr.aueb.cf.myreserva.dto.reservation.ReservationReadOnlyDTO;
import gr.aueb.cf.myreserva.dto.reservation.ReservationUserReadOnlyDTO;
import gr.aueb.cf.myreserva.dto.user.UserInsertDTO;
import gr.aueb.cf.myreserva.dto.user.UserReadOnlyDTO;
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
                user.getRole().toString().toLowerCase()
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
        user.setRole(null);

        return user;
    }

    public ReservationReadOnlyDTO mapToReservationReadOnlyDTO(Reservation reservation) {
        return new ReservationReadOnlyDTO(
                reservation.getId(),
                reservation.getDate(),
                reservation.getHours(),
                reservation.getText()
        );
    }

    public ReservationPublicReadOnlyDTO mapToReservationPublicReadOnlyDTO(Reservation reservation) {
        return new ReservationPublicReadOnlyDTO(
                reservation.getDate(),
                reservation.getHours()
        );
    }

    public ReservationUserReadOnlyDTO mapToReservationUserReadOnlyDTO(Reservation reservation) {
        return new ReservationUserReadOnlyDTO(
                reservation.getId(),
                reservation.getDate(),
                reservation.getHours(),
                reservation.getText(),
                mapToUserReadOnlyDTO(reservation.getUser())
        );
    }
}
