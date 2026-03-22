import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { SafeResourceUrlPipe } from './safe-resource-url.pipe';
import { BooksPageComponent } from './components/books-page/books-page.component';
import { HttpClientModule } from '@angular/common/http';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { ToastrModule } from 'ngx-toastr';
import { provideAnimationsAsync } from '@angular/platform-browser/animations/async';
import { SearchBarComponent } from './components/search-bar/search-bar.component';
import { Ng2SearchPipeModule } from 'ng2-search-filter';
import { BooksService } from './services/books.service';
import { FilterPipe } from './filter.pipe';
import { HomePageComponent } from './components/home-page/home-page.component';
import { LoginComponent } from './components/login/login.component';
import { MyAccountComponent } from './components/my-account/my-account.component';
import { SuggestBookComponent } from './components/suggest-book/suggest-book.component';
import { BooksDisplayComponent } from './components/books-display/books-display.component';
import { RouterModule, Routes } from '@angular/router';

import { UserNavComponent } from './components/user-nav/user-nav.component';
import { SuggestedBooksComponent } from './components/suggested-books/suggested-books.component';

const routes: Routes = [
  { path: '', redirectTo: '/login', pathMatch: 'full' },
  { path: 'login', component: LoginComponent },
  { path: 'home', component: HomePageComponent },
  { path: 'my-account', component: MyAccountComponent },
  { path: 'books', component: BooksDisplayComponent },
  { path: 'suggest-book', component: SuggestBookComponent },
  { path: 'suggested-books', component: SuggestedBooksComponent },
  { path: 'admin-home', component: HomePageComponent },
];

@NgModule({
  declarations: [
    AppComponent,
    SuggestBookComponent,
    SearchBarComponent,
    SafeResourceUrlPipe,
    FilterPipe,
    HomePageComponent,
    BooksPageComponent,
    LoginComponent,
    MyAccountComponent,
    SuggestBookComponent,
    BooksDisplayComponent,
    UserNavComponent,
    SuggestedBooksComponent,
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    HttpClientModule,
    FormsModule,
    ReactiveFormsModule,
    RouterModule.forRoot(routes),
  ],
  providers: [],
  bootstrap: [AppComponent],
})
export class AppModule {}
