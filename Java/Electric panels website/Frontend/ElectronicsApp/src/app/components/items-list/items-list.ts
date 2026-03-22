import { ActivatedRoute } from '@angular/router';
import { CommonModule, DecimalPipe } from '@angular/common';
import {ChangeDetectorRef, Component, OnDestroy} from '@angular/core';
import { Service } from '../services/service';
import { ItemModel } from '../models/itemModel';
import { MenuCategory } from '../models/menuCategory';
import {switchMap} from 'rxjs';

@Component({
  selector: 'app-items-list',
  standalone: true,
  imports: [CommonModule, DecimalPipe],
  templateUrl: './items-list.html',
  styleUrl: './items-list.scss'
})
export class ItemsList implements OnDestroy {

  categories: MenuCategory[] = [];
  items: ItemModel[] = [];
  filteredItems: ItemModel[] = [];
  currentType: string | null = null;

  // ── Stare coș ────────────────────────────────────────────────
  quantities: Record<number, number> = {};  // cantitate selectată per item
  justAdded: Record<number, boolean> = {};  // feedback verde buton
  adding: Record<number, boolean> = {};     // request în zbor
  toastVisible = false;
  private toastTimeout: any;

  constructor(
    private route: ActivatedRoute,
    private service: Service,
    private cartService: Service,
    private cdr: ChangeDetectorRef
  ) {}

  ngOnInit(): void {
    this.route.paramMap.pipe(
      switchMap(params => {
        this.currentType = params.get('type');
        return this.service.getItems();
      })
    ).subscribe(data => {
      this.items = data;
      this.filterItemsByType(this.currentType);
    });
  }

  ngOnDestroy(): void {
    clearTimeout(this.toastTimeout);
  }

  filterItemsByType(type: string | null | undefined): void {
    if (!type) {
      this.filteredItems = [...this.items];
    } else {
      const normalizedType = type.toLowerCase().trim();
      this.filteredItems = this.items.filter(item =>
        item.type?.toLowerCase().trim() === normalizedType
      );
    }
  }

  // ── Cantitate ────────────────────────────────────────────────
  getQty(item: ItemModel): number {
    if (item.id == null) return 1;
    return this.quantities[item.id] ?? 1;
  }

  incQty(item: ItemModel): void {
    if (item.id == null) return;
    const current = this.getQty(item);
    if (current < item.stockAvailable) {
      this.quantities[item.id] = current + 1;
    }
  }

  decQty(item: ItemModel): void {
    if (item.id == null) return;
    const current = this.getQty(item);
    if (current > 1) {
      this.quantities[item.id] = current - 1;
    }
  }

  // ── Adaugă în coș ────────────────────────────────────────────
  addToCart(item: ItemModel): void {
    if (item.id == null) return;
    const id = item.id;

    if (this.adding[id] || this.justAdded[id]) return;

    this.adding[id] = true;
    const qty = this.getQty(item);

    this.cartService.addToCart(id, qty).subscribe({
      next: () => {
        this.adding[id] = false;
        this.justAdded[id] = true;
        this.quantities[id] = 1; // resetează cantitatea după adăugare
        this.showToast();
        setTimeout(() => { this.justAdded[id] = false; }, 2000);
      },
      error: (err:any) => {
        this.adding[id] = false;
        console.error(err?.error?.message ?? 'Eroare la adăugarea în coș');
      }
    });
  }

  private showToast(): void {
    this.toastVisible = true;
    clearTimeout(this.toastTimeout);
    this.toastTimeout = setTimeout(() => { this.toastVisible = false; }, 2500);
  }
}
