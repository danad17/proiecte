package app.electronics.Cart;

import lombok.Data;

import java.util.List;

// ─────────────────────────────────────────────────────────────
// CartResponseDTO.java  –  ce returnăm la frontend
// ─────────────────────────────────────────────────────────────
@Data
public class CartResponseDTO {

    private Long cartId;
    private int userId;
    private List<CartItemDTO> items;
    private double total;
    private int totalItemCount;


    public CartResponseDTO(Long cartId, int userId,
                           List<CartItemDTO> items,
                           double total, int totalItemCount) {
        this.cartId = cartId;
        this.userId = userId;
        this.items = items;
        this.total = total;
        this.totalItemCount = totalItemCount;
    }
}

// ─────────────────────────────────────────────────────────────
// CartItemDTO.java  –  un rând din coș
// ─────────────────────────────────────────────────────────────
@Data
class CartItemDTO {

    private Long cartItemId;
    private Long itemId;
    private String name;
    private String description;
    private double price;
    private String image;
    private String brand;
    private String type;
    private String subtype;
    private String category;
    private Long stockAvailable;
    private int quantity;
    private double subtotal;
}

// ─────────────────────────────────────────────────────────────
// AddToCartRequest.java  –  body pentru POST /cart/add
// ─────────────────────────────────────────────────────────────
@Data
class AddToCartRequest {

    private Long itemId;
    private int quantity;

}

// ─────────────────────────────────────────────────────────────
// UpdateQuantityRequest.java  –  body pentru PUT /cart/items/{id}
// ─────────────────────────────────────────────────────────────
@Data
class UpdateQuantityRequest {

    private int quantity;

}
