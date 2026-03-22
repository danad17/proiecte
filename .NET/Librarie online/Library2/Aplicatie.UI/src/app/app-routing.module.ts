import { Component, NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { BooksPageComponent } from './components/books-page/books-page.component';
import { HomePageComponent } from './components/home-page/home-page.component';
import { LoginComponent } from './components/login/login.component';
import { MyAccountComponent } from './components/my-account/my-account.component';
import { BooksDisplayComponent } from './components/books-display/books-display.component';
import { SuggestBookComponent } from './components/suggest-book/suggest-book.component';
import { SuggestedBooksComponent } from './components/suggested-books/suggested-books.component';
//import path from 'path';

export const routes: Routes = [
  { path: '', redirectTo: '/login', pathMatch: 'full' },
  { path: 'login', component: LoginComponent },
  { path: 'home', component: HomePageComponent },
  { path: 'my-account', component: MyAccountComponent },
  { path: 'books', component: BooksDisplayComponent },
  { path: 'suggest-book', component: SuggestBookComponent },
  { path: 'suggested-books', component: SuggestedBooksComponent },
  { path: 'admin-home', component: HomePageComponent },
  { path: 'book-page/:name', component: BooksPageComponent },
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule],
})
export class AppRoutingModule {}
