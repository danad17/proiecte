package app.electronics.User;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    List<User> findByVerificationCodeExpiresAtBefore(LocalDateTime dateTime);
    @Modifying
    @Query("DELETE FROM User u WHERE u.verificationCodeExpiresAt < :now")

    Optional<User> findByName(String name);
    Optional<User> findByEmail(String name);
    Optional<User> findByResetToken(String token);
    boolean existsByEmail(String email);


}
