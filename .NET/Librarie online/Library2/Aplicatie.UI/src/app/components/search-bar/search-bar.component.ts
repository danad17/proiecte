// src/app/components/search-bar/search-bar.component.ts
import { Component, HostListener, OnInit } from '@angular/core';
import { BooksService } from '../../services/books.service';
import { Book } from '../../models/book.model';
import {
  FormBuilder,
  FormControl,
  FormGroup,
  ReactiveFormsModule,
} from '@angular/forms';
import { debounceTime, distinctUntilChanged, switchMap } from 'rxjs';
import { Router } from '@angular/router';

@Component({
  selector: 'app-search-bar',
  templateUrl: './search-bar.component.html',
  styleUrls: ['./search-bar.component.css'],
})
export class SearchBarComponent {
  books: Book[] = [];
  filteredBooks: Book[] = [];
  searchText: string = '';

  constructor(private booksService: BooksService, private router: Router) {}

  ngOnInit(): void {
    this.booksService.getAllBooks().subscribe(
      (books) => {
        this.books = books;
      },
      (err) => {
        console.log(err);
      }
    );
  }

  onSearchTextChanged(): void {
    if (this.searchText.trim() === '') {
      this.filteredBooks = [];
    } else {
      this.filteredBooks = this.books.filter((book) =>
        book.name.toLowerCase().includes(this.searchText.toLowerCase())
      );
    }
  }
  onBookClick(book: Book): void {
    this.router.navigate(['/books-page']);
  }
}
