import {Injectable} from '@angular/core';
import {HttpClient, HttpHeaders} from '@angular/common/http';
import {BehaviorSubject, Observable, of, tap} from 'rxjs';
import {ItemModel} from '../models/itemModel';
import {jwtDecode} from 'jwt-decode';
import {AddToCartRequest, CartResponseDTO, UpdateQuantityRequest} from '../models/cartModels';


@Injectable({
  providedIn: 'root',
})

export class Service{

  private apiUrl = 'http://localhost:8082';
  role: string | null = '';
  data: string = '';

  constructor(private http: HttpClient) {
    this.fetchCart().subscribe({ error: () => {} });
  }

  storeToken(token: string) {
    if (typeof window !== 'undefined') {
      localStorage.setItem('authToken', token);
    }
  }

  getToken(): string | null {
    if (typeof window !== 'undefined') {
      const token = localStorage.getItem('authToken');
      console.log('Token retrieved from localStorage:', token);
      return token;
    }
    return null;
  }

  decodeToken(token: string): any {
    return jwtDecode(token);
  }

  getRole():any{
    const token = this.getToken();
    if (token) {
      const data = this.decodeToken(token);
      console.log("Token decodat:", data);
      this.role = data.role;
      console.log("Rolul utilizatorului este:", this.role);
    }
    return this.role;
  }

  register(email: string, password: string): Observable<any> {
    return this.http.post(`${this.apiUrl}/auth/signup`, { email, password});
  }

  login(email: string, password: string): Observable<any> {
    return this.http.post(`${this.apiUrl}/auth/login`, { email, password }).pipe(
      tap((response: any) => {
        this.storeToken(response.token);
      })
    );
  }

  isLoggedIn(): boolean {
    if (typeof window !== 'undefined' && localStorage) {
      return !!localStorage.getItem('authToken');
    }
    return false;
  }

  verifyCode(email: string, code: string): Observable<any> {
    return this.http.post(`${this.apiUrl}/auth/verify`, {
      email,
      verificationCode: code
    }, { responseType: 'text' as 'json' });
  }


  resendCode(email: string): Observable<any> {
    return this.http.post(
      `${this.apiUrl}/auth/resend`,
      { email },
      { responseType: 'text' as 'json' }
    );
  }

  forgotPassword(email: string): Observable<any> {
    return this.http.post(`${this.apiUrl}/auth/forgot-password`, { email }, { responseType: 'text' as 'json' });
  }

  resetPassword(data: { token: string; newPassword: string }): Observable<any> {
    return this.http.post(`${this.apiUrl}/auth/reset-password`, data, { responseType: 'text' as 'json' });
  }

  logout() {
    localStorage.removeItem('authToken');
  }

  //ITEMS

  // în Service:
  private itemsCache: ItemModel[] | null = null;

  getItems(): Observable<ItemModel[]> {
    if (this.itemsCache) {
      return of(this.itemsCache);  // returnează instant din cache
    }
    return this.http.get<ItemModel[]>(`${this.apiUrl}/items/getAll`).pipe(
      tap(items => this.itemsCache = items)
    );
  }

  // getItems():Observable<ItemModel[]>{
  //   return this.http.get<ItemModel[]>(`${this.apiUrl}/items/getAll`)
  // }

  private readonly baseUrl = 'http://localhost:8082/cart';

  // State reactiv al coșului — orice componentă poate asculta
  private cartSubject = new BehaviorSubject<CartResponseDTO | null>(null);
  readonly cart$ = this.cartSubject.asObservable();



  // ── GET /cart ────────────────────────────────────────────────
  private getAuthHeaders(): HttpHeaders {
    return new HttpHeaders({ 'Authorization': `Bearer ${this.getToken()}` });
  }

  fetchCart(): Observable<CartResponseDTO> {
    return this.http.get<CartResponseDTO>(`${this.baseUrl}`, { headers: this.getAuthHeaders() })
      .pipe(tap(cart => this.cartSubject.next(cart)));
  }

  addToCart(itemId: number, quantity: number): Observable<CartResponseDTO> {
    const body: AddToCartRequest = { itemId, quantity };
    return this.http.post<CartResponseDTO>(`${this.baseUrl}/add`, body, { headers: this.getAuthHeaders() })
      .pipe(tap(cart => this.cartSubject.next(cart)));
  }

  updateQuantity(cartItemId: number, quantity: number): Observable<CartResponseDTO> {
    const body: UpdateQuantityRequest = { quantity };
    return this.http.put<CartResponseDTO>(
      `${this.baseUrl}/items/${cartItemId}`, body, { headers: this.getAuthHeaders() }
    ).pipe(tap(cart => this.cartSubject.next(cart)));
  }

  removeItem(cartItemId: number): Observable<CartResponseDTO> {
    return this.http.delete<CartResponseDTO>(
      `${this.baseUrl}/items/${cartItemId}`, { headers: this.getAuthHeaders() }
    ).pipe(tap(cart => this.cartSubject.next(cart)));
  }

  clearCart(): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/clear`, { headers: this.getAuthHeaders() }).pipe(
      tap(() => {
        const current = this.cartSubject.value;
        if (current) {
          this.cartSubject.next({ ...current, items: [], total: 0, totalItemCount: 0 });
        }
      })
    );
  }

  // Helper: numărul de produse din coș pentru badge navbar
  get itemCount(): number {
    return this.cartSubject.value?.totalItemCount ?? 0;
  }

  // Resetează coșul local (ex: la logout)
  resetCart(): void {
    this.cartSubject.next(null);
  }

}
