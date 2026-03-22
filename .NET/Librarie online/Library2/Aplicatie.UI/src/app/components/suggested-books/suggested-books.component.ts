import { Component } from '@angular/core';
import { Book } from '../../models/book.model';
import { BooksService } from '../../services/books.service';

@Component({
  selector: 'app-suggested-books',
  templateUrl: './suggested-books.component.html',
  styleUrl: './suggested-books.component.css',
})
export class SuggestedBooksComponent {
  books: Book[] = [];

  constructor(public service: BooksService) {}

  ngOnInit(): void {
    this.service.refreshList();
  }
}
