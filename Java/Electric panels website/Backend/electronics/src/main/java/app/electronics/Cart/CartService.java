package app.electronics.Cart;

import app.electronics.Items.ItemRepository;
import app.electronics.Items.Items;
import app.electronics.User.User;
import app.electronics.User.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class CartService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final ItemRepository itemRepository;          // repo-ul tău existent pentru Items
    private final UserRepository userRepository;          // repo-ul tău existent pentru User

    @Autowired
    public CartService(CartRepository cartRepository,
                       CartItemRepository cartItemRepository,
                       ItemRepository itemRepository,
                       UserRepository userRepository) {
        this.cartRepository = cartRepository;
        this.cartItemRepository = cartItemRepository;
        this.itemRepository = itemRepository;
        this.userRepository = userRepository;
    }

    // ── Obține sau creează coșul pentru un user ───────────────────────────────
    public CartResponseDTO getOrCreateCart(int userId) {
        Cart cart = cartRepository.findByUserId(userId)
                .orElseGet(() -> createCartForUser(userId));
        return toDTO(cart);
    }

    // ── Adaugă un item în coș ────────────────────────────────────────────────
    public CartResponseDTO addToCart(int userId, Long itemId, int quantity) {
        if (quantity <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Cantitatea trebuie să fie mai mare decât 0");
        }

        // Găsește sau creează coșul
        Cart cart = cartRepository.findByUserId(userId)
                .orElseGet(() -> createCartForUser(userId));

        // Validează că item-ul există (folosind ItemRepository-ul tău existent)
        Items item = itemRepository.findById((int) (long) itemId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Item-ul nu există"));

        // Verifică stoc
        if (item.getStockAvailable() < quantity) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Stoc insuficient. Disponibil: " + item.getStockAvailable());
        }

        // Dacă item-ul e deja în coș → actualizează cantitatea
        cartItemRepository.findByCartIdAndItemId(cart.getId(), itemId)
                .ifPresentOrElse(
                        existingCartItem -> {
                            int newQty = existingCartItem.getQuantity() + quantity;
                            if (item.getStockAvailable() < newQty) {
                                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                                        "Stoc insuficient pentru cantitatea dorită");
                            }
                            existingCartItem.setQuantity(newQty);
                            cartItemRepository.save(existingCartItem);
                        },
                        () -> {
                            // Item nou în coș
                            CartItem newCartItem = new CartItem();
                            newCartItem.setCart(cart);
                            newCartItem.setItem(item);
                            newCartItem.setQuantity(quantity);
                            cartItemRepository.save(newCartItem);
                        }
                );

        // Returnează coșul actualizat
        Cart updatedCart = cartRepository.findById(cart.getId()).orElseThrow();
        return toDTO(updatedCart);
    }

    // ── Actualizează cantitatea unui CartItem ────────────────────────────────
    public CartResponseDTO updateQuantity(int userId, Long cartItemId, int newQuantity) {
        Cart cart = cartRepository.findByUserId(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Coșul nu există"));

        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Cart item-ul nu există"));

        // Securitate: verifică că item-ul aparține coșului acestui user
        if (!cartItem.getCart().getId().equals(cart.getId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN,
                    "Acces interzis la acest cart item");
        }

        if (newQuantity <= 0) {
            // Dacă cantitatea e 0 sau negativă → șterge item-ul
            cartItemRepository.delete(cartItem);
        } else {
            // Verifică stoc pentru noua cantitate
            if (cartItem.getItem().getStockAvailable() < newQuantity) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "Stoc insuficient. Disponibil: " + cartItem.getItem().getStockAvailable());
            }
            cartItem.setQuantity(newQuantity);
            cartItemRepository.save(cartItem);
        }

        Cart updatedCart = cartRepository.findById(cart.getId()).orElseThrow();
        return toDTO(updatedCart);
    }

    // ── Șterge un item din coș ───────────────────────────────────────────────
    public CartResponseDTO removeFromCart(int userId, Long cartItemId) {
        Cart cart = cartRepository.findByUserId(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Coșul nu există"));

        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Cart item-ul nu există"));

        if (!cartItem.getCart().getId().equals(cart.getId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN,
                    "Acces interzis la acest cart item");
        }

        cartItemRepository.delete(cartItem);

        Cart updatedCart = cartRepository.findById(cart.getId()).orElseThrow();
        return toDTO(updatedCart);
    }

    // ── Golește tot coșul ────────────────────────────────────────────────────
    public void clearCart(int userId) {
        Cart cart = cartRepository.findByUserId(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Coșul nu există"));

        cartItemRepository.deleteAllByCartId(cart.getId());
    }

    // ── Helper: crează coș nou pentru user ──────────────────────────────────
    private Cart createCartForUser(int userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "User-ul nu există"));

        Cart newCart = new Cart();
        newCart.setUser(user);
        return cartRepository.save(newCart);
    }

    // ── Mapper: Cart → CartResponseDTO ──────────────────────────────────────
    private CartResponseDTO toDTO(Cart cart) {
        List<CartItemDTO> itemDTOs = cart.getCartItems().stream()
                .map(this::toItemDTO)
                .collect(Collectors.toList());

        return new CartResponseDTO(
                cart.getId(),
                cart.getUser().getId(),
                itemDTOs,
                cart.getTotal(),
                cart.getTotalItemCount()
        );
    }

    private CartItemDTO toItemDTO(CartItem cartItem) {
        Items item = cartItem.getItem();
        CartItemDTO dto = new CartItemDTO();
        dto.setCartItemId(cartItem.getId());
        dto.setItemId(item.getId());
        dto.setName(item.getName());
        dto.setDescription(item.getDescription());
        dto.setPrice(item.getPrice());
        dto.setImage(item.getImage());
        dto.setBrand(item.getBrand());
        dto.setType(item.getType());
        dto.setSubtype(item.getSubtype());
        dto.setCategory(item.getCategory());
        dto.setStockAvailable(item.getStockAvailable());
        dto.setQuantity(cartItem.getQuantity());
        dto.setSubtotal(cartItem.getSubtotal());
        return dto;
    }
}

