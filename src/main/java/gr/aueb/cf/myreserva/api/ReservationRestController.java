package gr.aueb.cf.myreserva.api;

import gr.aueb.cf.myreserva.core.exceptions.AppObjectAlreadyExists;
import gr.aueb.cf.myreserva.core.exceptions.AppObjectNotAuthorizedException;
import gr.aueb.cf.myreserva.core.exceptions.AppObjectNotFoundException;
import gr.aueb.cf.myreserva.core.exceptions.ValidationException;
import gr.aueb.cf.myreserva.dto.ApiResponse;
import gr.aueb.cf.myreserva.dto.reservation.ReservationInsertDTO;
import gr.aueb.cf.myreserva.dto.reservation.ReservationPublicReadOnlyDTO;
import gr.aueb.cf.myreserva.dto.reservation.ReservationReadOnlyDTO;
import gr.aueb.cf.myreserva.dto.reservation.ReservationUserReadOnlyDTO;
import gr.aueb.cf.myreserva.model.User;
import gr.aueb.cf.myreserva.service.ReservationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;


@RestController
@RequestMapping("/reservations")
@RequiredArgsConstructor
public class ReservationRestController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ReservationRestController.class);
    private final ReservationService reservationService;


    /**
     * CREATE A NEW RESERVATION
     */
    @PostMapping
    public ResponseEntity<ApiResponse<Void>> newReservation(
            @Valid @RequestBody ReservationInsertDTO insertDTO,
            Authentication authentication,
            BindingResult bindingResult
    ) {
        try {
            if (bindingResult.hasErrors()) {
                throw new ValidationException(bindingResult);
            }

            User user = (User) authentication.getPrincipal();

            reservationService.newReservation(insertDTO, user);

            ApiResponse<Void> apiResponse = new ApiResponse<>(
                    true,
                    "Reservation confirmed",
                    null
            );

            return ResponseEntity.status(HttpStatus.CREATED).body(apiResponse);

        } catch(ValidationException e) {
            LOGGER.warn("Invalid reservation format. Errors: {}", e.getBindingResult());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (AppObjectAlreadyExists e) {
            LOGGER.warn("Reservation at given date and time already exists.");
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            System.out.println(e.getMessage());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }


    /**
     * GET ALL RESERVATIONS PAGINATED Admin only
     */
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Page<ReservationReadOnlyDTO>> getReservationsPaginated(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "1") int size
    ) {
        Page<ReservationReadOnlyDTO> reservationsPage = reservationService.getPaginatedReservations(page, size);
        return new ResponseEntity<>(reservationsPage, HttpStatus.OK);
    }


    /**
     * GET CURRENT LOGGED IN USER'S RESERVATIONS
     */
    @GetMapping("/current")
    public ResponseEntity<List<ReservationReadOnlyDTO>> getReservationsByCurrentUser(Authentication authentication) {

        String email = authentication.getName();

        List<ReservationReadOnlyDTO> reservations = reservationService.getReservationsByCurrentUser(email);

        return new ResponseEntity<>(reservations, HttpStatus.OK);
    }


    /**
     * GET RESERVATIONS (Reserved Hours) BY DATE. USER DATA EXCLUDED. User only
     * For updating reserved times on user's new-reservation page.
     */
    @GetMapping("/reserved/{date}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<List<ReservationPublicReadOnlyDTO>> getReservationsByDate(
            @PathVariable
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate date
    ) {
        List<ReservationPublicReadOnlyDTO> reservations = reservationService.getReservationsByDate(date);
        return new ResponseEntity<>(reservations, HttpStatus.OK);
    }


    /**
     * GET RESERVATIONS BY DATE WITH USER DATA Admin only
     * For Admin Dashboard
     */
    @GetMapping("/{date}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<ReservationUserReadOnlyDTO>> getReservationsWithUserByDate(
            @PathVariable
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate date
    ) {
        List<ReservationUserReadOnlyDTO> reservations = reservationService.getReservationsWithUserByDate(date);
        return new ResponseEntity<>(reservations, HttpStatus.OK);
    }


    /**
     * DELETE RESERVATION BY ID
     */
    @DeleteMapping("/{reservationId}")
    public ResponseEntity<ApiResponse<Void>> deleteReservationById(
            @PathVariable Long reservationId,
            Authentication authentication
    ) throws AppObjectNotFoundException {
        try {
            String email = authentication.getName();

            reservationService.deleteReservationById(reservationId, email);

            ApiResponse<Void> apiResponse = new ApiResponse<>(
                    true,
                    "Reservation deleted successfully",
                    null
            );
            return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
        } catch(AppObjectNotFoundException e) {
            LOGGER.warn("Reservation with id: {} not found to delete.", reservationId);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (AppObjectNotAuthorizedException e) {
            LOGGER.warn("Reservation with id: {} does not belong to logged in user.", reservationId);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }
}
