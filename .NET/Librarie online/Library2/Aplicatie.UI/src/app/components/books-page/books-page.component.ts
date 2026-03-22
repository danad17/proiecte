import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Book } from '../../models/book.model';
import { BooksService } from '../../services/books.service';
@Component({
  selector: 'app-books-page',
  templateUrl: './books-page.component.html',
  styleUrls: ['./books-page.component.css'],
  template: '<div *ngIf="visible">Hi</div>',
})
export class BooksPageComponent implements OnInit {
  books: Book[] = [];
  bookName: string = '';

  constructor(
    private route: ActivatedRoute,
    public booksService: BooksService
  ) {}

  ngOnInit(): void {
    this.route.params.subscribe((params) => {
      this.bookName = params['name'];
      console.log('Route Book Name:', this.bookName);

      if (this.bookName) {
        this.booksService.getBookByName(this.bookName).subscribe((books) => {
          this.books = books;
          console.log('Books:', this.books);
        });
      }
    });
  }
}
