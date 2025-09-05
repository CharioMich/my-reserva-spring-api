package gr.aueb.cf.myreserva.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "reservations")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Reservation extends AbstractEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(nullable = false)
    LocalDateTime dateTime;

    @Column(nullable = false)
    String hours;

    @Column(nullable = true)
    String text;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false) // Foreign key for users
    User user;

}
