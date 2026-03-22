import {Component, OnDestroy, OnInit, ChangeDetectorRef} from '@angular/core';
import {Service} from '../services/service';
import {Router} from '@angular/router';
import {Subscription} from 'rxjs';
import {CartItemDTO, CartResponseDTO} from '../models/cartModels';
import {CommonModule, DecimalPipe} from '@angular/common';

@Component({
  selector: 'app-cart',
  standalone: true,
  imports: [CommonModule, DecimalPipe],
  templateUrl: './shopping-cart.html',
  styleUrl: './shopping-cart.scss'
})
export class ShoppingCart implements OnInit, OnDestroy {

  cart: CartResponseDTO | null = null;
  isLoading = true;
  errorMessage: string | null = null;

  private cartSub!: Subscription;

  constructor(
    private cartService: Service,
    private router: Router,
    private cdr: ChangeDetectorRef
  ) {}

  ngOnInit(): void {
    this.cartSub = this.cartService.cart$.subscribe(cart => {
      this.cart = cart;
      this.isLoading = false;
      this.cdr.markForCheck();
    });

    this.cartService.fetchCart().subscribe({
      error: () => {
        this.errorMessage = 'Nu s-a putut încărca coșul. Încearcă din nou.';
        this.isLoading = false;
      }
    });
  }

  ngOnDestroy(): void {
    this.cartSub?.unsubscribe();
  }

  goToShop(): void {
    this.router.navigate(['/']);
  }

  get isEmpty(): boolean {
    return !this.cart || this.cart.items.length === 0;
  }

  checkout(): void {
    this.router.navigate(['/checkout']);
  }

  increaseQty(item: CartItemDTO): void {
    if (item.quantity >= item.stockAvailable) return;
    this.cartService.updateQuantity(item.cartItemId, item.quantity + 1).subscribe({
      next: () => {
        this.cartService.fetchCart().subscribe();
        // this.cdr.markForCheck();
      },
      error: err => console.error('Eroare update cantitate:', err)
    });
  }

  decreaseQty(item: CartItemDTO): void {
    if (item.quantity <= 1) return;
    this.cartService.updateQuantity(item.cartItemId, item.quantity - 1).subscribe({
      next: () => {
        this.cartService.fetchCart().subscribe();
        // this.cdr.markForCheck();
      },
      error: err => console.error('Eroare update cantitate:', err)
    });
  }

  removeItem(item: CartItemDTO): void {
    this.cartService.removeItem(item.cartItemId).subscribe({
      next: () => {
        this.cartService.fetchCart().subscribe();
        setTimeout(() => {});
      },
      error: err => console.error('Eroare ștergere item:', err)
    });
  }

  clearCart(): void {
    if (!confirm('Ești sigur că vrei să golești coșul?')) return;
    this.cartService.clearCart().subscribe({
      next: () => {
        this.cartService.fetchCart().subscribe();
        setTimeout(() => {});
      },
      error: err => console.error('Eroare golire coș:', err)
    });
  }
}
