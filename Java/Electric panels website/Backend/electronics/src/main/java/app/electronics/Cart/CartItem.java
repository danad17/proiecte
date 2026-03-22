package app.electronics.Cart;

import app.electronics.Items.Items;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "cart_item")
public class CartItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Referință la coș
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cart_id", nullable = false)
    private Cart cart;

    // Referință la entitatea ta existentă Items
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "item_id", nullable = false)
    private Items item;

    @Column(nullable = false)
    private int quantity;

    // Subtotal calculat: price * quantity
    public double getSubtotal() {
        return item.getPrice() * quantity;
    }
}

