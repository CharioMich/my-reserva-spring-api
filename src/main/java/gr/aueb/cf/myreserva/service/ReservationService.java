package gr.aueb.cf.myreserva.service;

import gr.aueb.cf.myreserva.core.exceptions.AppObjectAlreadyExists;
import gr.aueb.cf.myreserva.core.exceptions.AppObjectNotAuthorizedException;
import gr.aueb.cf.myreserva.core.exceptions.AppObjectNotFoundException;
import gr.aueb.cf.myreserva.dto.reservation.ReservationInsertDTO;
import gr.aueb.cf.myreserva.dto.reservation.ReservationPublicReadOnlyDTO;
import gr.aueb.cf.myreserva.dto.reservation.ReservationReadOnlyDTO;
import gr.aueb.cf.myreserva.dto.reservation.ReservationUserReadOnlyDTO;
import gr.aueb.cf.myreserva.mapper.Mapper;
import gr.aueb.cf.myreserva.model.Reservation;
import gr.aueb.cf.myreserva.model.User;
import gr.aueb.cf.myreserva.repository.ReservationRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final Mapper mapper;

    @Transactional(rollbackOn = {AppObjectAlreadyExists.class})
    public void newReservation(ReservationInsertDTO insertDTO, User user) throws AppObjectAlreadyExists {
        if (reservationRepository.findByDateAndHours(insertDTO.date(), insertDTO.hours()).isPresent())
            throw new AppObjectAlreadyExists("Reservation", "A reservation at time: " + insertDTO.hours() + " already exists");

        Reservation reservation = new Reservation(
                null,
                insertDTO.date(),
                insertDTO.hours(),
                insertDTO.text(),
                user
        );
        reservationRepository.save(reservation);
    }


    @Transactional
    public Page<ReservationReadOnlyDTO> getPaginatedReservations(int page, int size) {
        String defaultSort = "id";
        Pageable pageable = PageRequest.of(page, size, Sort.by(defaultSort).ascending());
        return reservationRepository.findAll(pageable).map(mapper::mapToReservationReadOnlyDTO);
    }


    public List<ReservationReadOnlyDTO> getReservationsByCurrentUser(String email) {
        return reservationRepository.findByUserEmail(email)
                .stream()
                .map(mapper::mapToReservationReadOnlyDTO)
                .collect(Collectors.toList());
    }


    public List<ReservationPublicReadOnlyDTO> getReservationsByDate(LocalDate date) {
        return reservationRepository.findByDate(date)
                .stream()
                .map(mapper::mapToReservationPublicReadOnlyDTO)
                .collect(Collectors.toList());
    }


    public List<ReservationUserReadOnlyDTO> getReservationsWithUserByDate(LocalDate date) {
        return reservationRepository.findByDate(date)
                .stream()
                .map(mapper::mapToReservationUserReadOnlyDTO)
                .collect(Collectors.toList());
    }


    public void deleteReservationById(Long reservationId, String email) throws AppObjectNotFoundException, AppObjectNotAuthorizedException {
        Reservation reservation = reservationRepository.findById(reservationId).orElseThrow(
                () -> new AppObjectNotFoundException("Reservation", "Reservation with id: " + reservationId + " not found for delete."));
        // Check if user is trying to delete someone else's reservation
        if (!reservation.getUser().getEmail().equals(email)) {
            throw new AppObjectNotAuthorizedException("Reservation", "Reservation with id: " + reservationId + " does not belong to logged in user.");
        }
        reservationRepository.deleteById(reservationId);
    }

}
