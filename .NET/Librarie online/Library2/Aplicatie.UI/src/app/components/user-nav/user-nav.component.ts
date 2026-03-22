import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-user-nav',
  templateUrl: './user-nav.component.html',
  styleUrl: './user-nav.component.css',
})
export class UserNavComponent implements OnInit {
  role: string | null = '';

  ngOnInit(): void {
    this.role = localStorage.getItem('role');
  }
}
