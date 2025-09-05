package gr.aueb.cf.myreserva.repository;

import gr.aueb.cf.myreserva.model.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ReservationRepository extends JpaRepository<Reservation, Long>, JpaSpecificationExecutor<Reservation> {

    @Query("SELECT r FROM Reservation r JOIN FETCH r.user " +
            "WHERE FUNCTION('DATE', r.dateTime) = :date")
    List<Reservation> findByDateWithUser(@Param("date") LocalDate date);

    List<Reservation> findByUserId(Long id);
}
