import { Component, signal } from '@angular/core';
import {NavigationEnd, RouterOutlet} from '@angular/router';
import {NavBar} from './components/nav-bar/nav-bar';
import {NgIf} from '@angular/common';
import {Router} from '@angular/router';

@Component({
  selector: 'app-root',
  imports: [RouterOutlet, NavBar],
  templateUrl: './app.html',
  styleUrl: './app.scss'
})
export class App {
  protected readonly title = signal('ElectronicsApp');
  showNavBar = true;

  constructor(private router: Router) {
    // this.router.events.subscribe((event) => {
    //   if (event instanceof NavigationEnd) {
    //     this.showNavBar = !this.router.url.includes('/login');
    //   }
    // });
  }
}
