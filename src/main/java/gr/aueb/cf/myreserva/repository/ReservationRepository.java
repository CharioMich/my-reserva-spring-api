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

    // JPQL
    @Query("SELECT r FROM Reservation r JOIN FETCH r.user " +
            "WHERE FUNCTION('DATE', r.date) = :date")
    List<Reservation> findByDateWithUser(@Param("date") LocalDate date);

//    For Eager Fetch
//    @Query("SELECT r FROM Reservation r JOIN FETCH r.user u " +
//            "WHERE u.email = :email")
    List<Reservation> findByUserEmail(String email);

    List<Reservation> findByDate(LocalDate date);

    Optional<Reservation> findByDateAndHours(LocalDate date, String hours);
}
