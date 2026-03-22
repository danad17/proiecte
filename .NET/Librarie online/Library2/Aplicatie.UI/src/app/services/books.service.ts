import { Injectable } from '@angular/core';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { Book } from '../models/book.model';
import { Observable, throwError } from 'rxjs';
import { catchError } from 'rxjs/operators';
import { NgForm } from '@angular/forms';
import { map } from 'rxjs/operators';
import { User } from '../models/user.mode';
import { LoginModel } from '../models/login.model';

@Injectable({
  providedIn: 'root',
})
export class BooksService {
  baseApiUrl: string = 'https://localhost:7038/api/books';
  baseApiUrl2: string = 'https://localhost:7038/api/Registration/Register';
  baseApiUrl3: string = 'https://localhost:7038/api/Registration/Login';
  baseApiUrl4: string = 'https://localhost:7038/api/Registration';
  list: Book[] = [];
  formData: Book = new Book();
  constructor(private http: HttpClient) {}
  refreshList() {
    this.http.get(this.baseApiUrl).subscribe({
      next: (res) => {
        this.list = res as Book[];
      },
      error: (err) => {
        console.log(err);
      },
    });
  }
  postBook() {
    return this.http.post(this.baseApiUrl, this.formData);
  }
  getBookById(id: string): Observable<Book> {
    return this.http.get<Book>(`${id}`);
  }

  searchBooks(query: string): Observable<Book[]> {
    return this.http.get<Book[]>(`${this.baseApiUrl}?q=${query}`);
  }
  getAllBooks(): Observable<Book[]> {
    return this.http.get<Book[]>(this.baseApiUrl);
  }
  getBookByName(name: string): Observable<Book[]> {
    return this.http.get<Book[]>(`${this.baseApiUrl}/name/${name}`);
  }
  getCurrentUser(): Observable<User> {
    return this.http.get<User>(`${this.baseApiUrl4}/CurrentUser`, {
      withCredentials: true,
    });
  }

  searchBook(params: any) {
    return this.http.post<Book[]>(this.baseApiUrl, JSON.stringify(params)).pipe(
      map((result) => {
        return result;
      })
    );
  }
  register(user: User): Observable<User> {
    return this.http.post<User>(this.baseApiUrl2, user);
  }
  login(user: LoginModel): Observable<any> {
    return this.http.post<any>(`${this.baseApiUrl4}/Login`, user, {
      withCredentials: true,
    });
  }
  resetForm(form: NgForm) {
    form.form.reset();
    this.formData = new Book();
  }
}
