package gr.aueb.cf.myreserva.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name = "reservations")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Reservation extends AbstractEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate date;

    @Column(nullable = false)
    private String hours;

    @Column(nullable = true)
    private String text;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false) // Foreign key for users
    private User user;

}
