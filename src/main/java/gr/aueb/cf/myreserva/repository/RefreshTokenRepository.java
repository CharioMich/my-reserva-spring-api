package gr.aueb.cf.myreserva.repository;

import gr.aueb.cf.myreserva.model.RefreshToken;
import gr.aueb.cf.myreserva.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByToken(String token);
    Optional<RefreshToken> findByUser(User user);
}
