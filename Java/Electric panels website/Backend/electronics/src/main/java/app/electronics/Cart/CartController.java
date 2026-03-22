package app.electronics.Cart;

import app.electronics.JwtConfiguration.JwtService;
import app.electronics.User.User;
import app.electronics.User.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/cart")
public class CartController {

    private final CartService cartService;
    private final JwtService jwtService;
    private final UserRepository userRepository;

    public CartController(CartService cartService, JwtService jwtService, UserRepository userRepository) {
        this.cartService = cartService;
        this.jwtService = jwtService;
        this.userRepository = userRepository;
    }

    @GetMapping
    public ResponseEntity<CartResponseDTO> getCart(HttpServletRequest request) {
        User user = getCurrentUser(request);
        return ResponseEntity.ok(cartService.getOrCreateCart(user.getId()));
    }

    @PostMapping("/add")
    public ResponseEntity<CartResponseDTO> addToCart(@RequestBody AddToCartRequest body, HttpServletRequest request) {
        User user = getCurrentUser(request);
        return ResponseEntity.ok(cartService.addToCart(user.getId(), body.getItemId(), body.getQuantity()));
    }

    @PutMapping("/items/{cartItemId}")
    public ResponseEntity<CartResponseDTO> updateQuantity(@PathVariable Long cartItemId, @RequestBody UpdateQuantityRequest body, HttpServletRequest request) {
        User user = getCurrentUser(request);
        return ResponseEntity.ok(cartService.updateQuantity(user.getId(), cartItemId, body.getQuantity()));
    }

    @DeleteMapping("/items/{cartItemId}")
    public ResponseEntity<CartResponseDTO> removeItem(@PathVariable Long cartItemId, HttpServletRequest request) {
        User user = getCurrentUser(request);
        return ResponseEntity.ok(cartService.removeFromCart(user.getId(), cartItemId));
    }

    @DeleteMapping("/clear")
    public ResponseEntity<Void> clearCart(HttpServletRequest request) {
        User user = getCurrentUser(request);
        cartService.clearCart(user.getId());
        return ResponseEntity.noContent().build();
    }

    // ── Helper ───────────────────────────────────────────────
    private User getCurrentUser(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        System.out.println("Auth header primit: " + authHeader); // ← adaugă temporar

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Token lipsă");
        }

        String jwt = authHeader.substring(7);
        System.out.println("JWT extras: '" + jwt + "'"); // ← adaugă temporar

        String email = jwtService.extractUsername(jwt);
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Userul nu există"));
    }
}

