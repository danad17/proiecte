import {Component, ElementRef, inject, QueryList, ViewChildren} from '@angular/core';
import {Service} from '../services/service';
import {ItemModel} from '../models/itemModel';
import {MenuCategory} from '../models/menuCategory';
import {CommonModule, DecimalPipe, NgFor, NgIf, NgOptimizedImage} from '@angular/common';
import {Slide} from '../models/slideModel';
import {Route, Router} from '@angular/router';

@Component({
  selector: 'app-home-page',
  imports: [CommonModule, NgIf, NgFor,DecimalPipe],
  templateUrl: './home-page.html',
  styleUrl: './home-page.scss',
})

export class HomePage {

  slides: Slide[] = [
    { image: 'https://i0.wp.com/xelectric.ro/wp-content/uploads/2021/04/SAM_2352-scaled.jpg?fit=2560%2C1485&ssl=1', title: 'Reduceri până la 20%', subtitle: 'Produse electrice în ofertă!' },
    { image: 'https://i0.wp.com/xelectric.ro/wp-content/uploads/2025/07/pompa-submersibila-front-scaled.jpg?fit=2560%2C1441&ssl=1', title: 'Promoții speciale', subtitle: 'Vezi ofertele noastre' },
    { image: 'https://i0.wp.com/xelectric.ro/wp-content/uploads/2025/07/pompa-submersibila-front-scaled.jpg?fit=2560%2C1441&ssl=1', title: 'Livrare rapidă', subtitle: 'Toate comenzile expediate în 24h' }
  ];

  items: ItemModel[] = [];
  isOpen = false;
  menuCategories: MenuCategory[] = [];
  selectedSubItems: ItemModel[] = [];
  hoveredCategory: MenuCategory | null = null;
  slideIndex: number = 0;
  @ViewChildren('slide') slideElements!: QueryList<ElementRef<HTMLDivElement>>;
  currentIndex: number = 0;
  autoplayInterval: any;
  selectedTab: 'ProduseRecomandate' | 'ProduseLaReducere' | 'ProduseCuRatingRidicat' = 'ProduseRecomandate';

  // ── Stare pentru feedback vizual buton coș ───────────────────
  // justAdded[item.id] = true timp de 2s după adăugare
  justAdded: Record<number, boolean> = {};
  // adding[item.id] = true cât timp request-ul e în zbor
  adding: Record<number, boolean> = {};
  // Toast notification vizibilă
  toastVisible = false;
  private toastTimeout: any;

  constructor(
    private service: Service,
    private router: Router,
    // ← injectat
  ) {}

  ngOnInit(): void {
    this.getItems();
  }

  goToAllProducts(): void {
    this.router.navigate(['/itemsList',]);
  }

  toggleMenu(): void {
    this.isOpen = !this.isOpen;
  }

  getItems(): void {
    this.service.getItems().subscribe({
      next: (data: ItemModel[]) => {
        this.items = data;
        this.buildMenu(data);
        if (this.items.length < 0) {
          console.error('Nu exista iteme:');
        }
      },
      error: error => {
        console.error('Eroare la obținerea itemelor:', error);
      }
    });
  }

  toggleHover(cat: MenuCategory | null): void {
    this.hoveredCategory = cat;
  }

  buildMenu(items: ItemModel[]): void {
    const map = new Map<string, Map<string, ItemModel[]>>();

    items.forEach(item => {
      if (!map.has(item.type)) map.set(item.type, new Map());
      const subMap = map.get(item.type)!;
      if (!subMap.has(item.subtype)) subMap.set(item.subtype, []);
      subMap.get(item.subtype)!.push(item);
    });

    this.menuCategories = Array.from(map.entries()).map(([type, subMap]) => ({
      type,
      open: false,
      subtypes: Array.from(subMap.entries()).map(([name, items]) => ({ name, items }))
    }));
  }

  ngAfterViewInit(): void {
    this.showSlide(this.currentIndex);
    this.startAutoPlay();
  }

  showSlide(index: number): void {
    const slidesArray = this.slideElements.toArray();
    slidesArray.forEach((slideRef, i) => {
      slideRef.nativeElement.style.opacity = i === index ? '1' : '0';
      slideRef.nativeElement.style.zIndex = i === index ? '10' : '0';
    });
    this.currentIndex = index;
  }

  nextSlide(): void {
    this.showSlide((this.currentIndex + 1) % this.slides.length);
  }

  prevSlide(): void {
    this.showSlide((this.currentIndex - 1 + this.slides.length) % this.slides.length);
  }

  goToSlide(i: number): void {
    this.showSlide(i);
  }

  startAutoPlay(): void {
    this.autoplayInterval = setInterval(() => this.nextSlide(), 4000);
  }

  stopAutoPlay(): void {
    clearInterval(this.autoplayInterval);
  }

  selectCategoryRouter(selectedCategory: MenuCategory): void {
    console.log('CLICKED TYPE:', selectedCategory.type);
    this.router.navigate(['/itemsList', selectedCategory.type]);
  }

  // ── Adaugă în coș (nou) ──────────────────────────────────────
  addToCart(item: ItemModel): void {
    if (item.id == null) return;         // ← adaugă guard
    const id = item.id;

    if (this.adding[id] || this.justAdded[id]) return;
    this.adding[id] = true;

    this.service.addToCart(id, 1).subscribe({
      next: () => {
        this.adding[id] = false;
        this.justAdded[id] = true;
        this.showToast();
        setTimeout(() => { this.justAdded[id] = false; }, 2000);
      },
      error: (err) => {
        this.adding[id] = false;
        console.error(err?.error?.message ?? 'Eroare la adăugarea în coș');
      }
    });
  }

  private showToast(): void {
    this.toastVisible = true;
    clearTimeout(this.toastTimeout);
    this.toastTimeout = setTimeout(() => {
      this.toastVisible = false;
    }, 2500);
  }

  ngOnDestroy(): void {
    clearInterval(this.autoplayInterval);
    clearTimeout(this.toastTimeout);
  }
}
