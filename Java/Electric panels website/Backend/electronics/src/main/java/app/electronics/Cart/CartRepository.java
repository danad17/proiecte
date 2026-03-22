package app.electronics.Cart;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {

    // Găsește coșul unui user după ID-ul userului
    Optional<Cart> findByUserId(int userId);

    // Verifică dacă un user are deja coș
    boolean existsByUserId(int userId);
}


