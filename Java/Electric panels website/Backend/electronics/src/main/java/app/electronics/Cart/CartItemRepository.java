package app.electronics.Cart;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {

    // Găsește un CartItem specific dintr-un coș după item_id
    Optional<CartItem> findByCartIdAndItemId(Long cartId, Long itemId);

    // Șterge toate item-urile unui coș
    void deleteAllByCartId(Long cartId);
}
