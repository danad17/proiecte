import { Component } from '@angular/core';
import {Service} from '../services/service';
import {Router} from '@angular/router';

@Component({
  selector: 'app-my-account',
  imports: [],
  templateUrl: './my-account.html',
  styleUrl: './my-account.scss',
})
export class MyAccount {

    constructor(private services: Service, private router: Router) {
    }

  logout() {
    this.services.logout();
    this.router.navigate(['/home']);
  }

}
