import { Component } from '@angular/core';
import { Book } from '../../models/book.model';
import { BooksService } from '../../services/books.service';

@Component({
  selector: 'app-books-display',
  templateUrl: './books-display.component.html',
  styleUrl: './books-display.component.css',
})
export class BooksDisplayComponent {
  books: Book[] = [];

  constructor(public service: BooksService) {}

  ngOnInit(): void {
    this.service.refreshList();
  }
}
