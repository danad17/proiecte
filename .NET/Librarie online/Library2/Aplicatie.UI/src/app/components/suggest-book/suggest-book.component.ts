import { Component } from '@angular/core';
import { BooksService } from '../../services/books.service';
import { NgForm } from '@angular/forms';
import { Book } from '../../models/book.model';

@Component({
  selector: 'app-suggest-book',
  templateUrl: './suggest-book.component.html',
  styleUrl: './suggest-book.component.css',
})
export class SuggestBookComponent {
  constructor(public service: BooksService) {}

  onSubmit(form: NgForm) {
    this.service.postBook().subscribe({
      next: (res) => {
        this.service.list = res as Book[];
        this.service.resetForm(form);
      },
      error: (err) => {
        console.log(err);
      },
    });
  }
}
