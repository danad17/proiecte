import { Component } from '@angular/core';
import { User } from '../../models/user.mode';
import { HttpClient } from '@angular/common/http';
import { Book } from '../../models/book.model';

@Component({
  selector: 'app-my-account',
  templateUrl: './my-account.component.html',
  styleUrl: './my-account.component.css',
})
export class MyAccountComponent {
  user: User | null = null;
  loading = false;

  constructor(private http: HttpClient) {}

  ngOnInit(): void {
    const userIdString = sessionStorage.getItem('userId');
    if (userIdString) {
      const userId = parseInt(userIdString);
      console.log(userIdString);
      if (!isNaN(userId)) {
        this.loading = true;
        this.http
          .get<User>(
            `https://localhost:7038/api/Registration/GetUser/${userId}`
          )
          .subscribe(
            (response) => {
              this.user = response;
              this.loading = false;
            },
            (error) => {
              console.error('Error fetching user:', error);
              this.loading = false;
            }
          );
      } else {
        console.error('Invalid user ID:', userIdString);
      }
    } else {
      console.error('User ID not found in session storage');
    }
  }
}
