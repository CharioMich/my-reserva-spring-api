package gr.aueb.cf.myreserva.repository;

import gr.aueb.cf.myreserva.model.User;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {
    Optional<User> findByEmail(String email);

    boolean existsByEmail(@Email(message = "Invalid email") String email);
    boolean existsByPhoneNumber(@NotEmpty(message = "Phone number required") String phoneNumber);
}
