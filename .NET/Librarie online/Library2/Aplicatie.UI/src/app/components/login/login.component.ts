import { HttpClient } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { catchError, throwError } from 'rxjs';
import { BooksService } from '../../services/books.service';
import { User } from '../../models/user.mode';
import { LoginModel } from '../../models/login.model';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrl: './login.component.css',
})
export class LoginComponent {
  registerObj: User = {
    userName: '',
    password: '',
    role: 'User',
    firstName: '',
    lastName: '',
    email: '',
  };

  loginObj: LoginModel = {
    userName: '',
    password: '',
  };

  isRegister: boolean = false;

  constructor(private router: Router, private http: HttpClient) {}

  register() {
    this.http
      .post(
        'https://localhost:7038/api/Registration/Register',
        this.registerObj
      )
      .subscribe(
        () => {
          // Registration successful, navigate to login page
          this.isRegister = false;
          this.router.navigate(['/login']); // Navigate to login after registration
        },
        (error) => {
          console.error('Error during registration:', error);
          // Handle registration error, display user-friendly message
          // Example: this.errorMessage = 'Registration failed. Please try again.';
        }
      );
  }
  login() {
    this.http
      .post('https://localhost:7038/api/Registration/Login', this.loginObj)
      .subscribe(
        (res: any) => {
          if (res.result) {
            localStorage.setItem('role', res.role); // Store role in local storage
            sessionStorage.setItem('userId', res.userId.toString()); // Store user ID in session storage
            this.router.navigate(['/home']); // Navigate to home on success
          } else {
            // Display error message to user
            alert(res.message);
          }
        },
        (error) => {
          console.error('Error during login:', error);
          // Handle login error, display user-friendly message
          // Example: this.errorMessage = 'Login failed. Please try again.';
        }
      );
  }
}
