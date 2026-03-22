import { Routes } from '@angular/router';
import {HomePage} from './components/home-page/home-page';
import {LoginComponent} from './components/login/login.component';
import {MyAccount} from './components/my-account/my-account';
import {ShoppingCart} from './components/shopping-cart/shopping-cart';
import {ItemsList} from './components/items-list/items-list';

export const routes: Routes = [
  { path: '', redirectTo: 'home', pathMatch: 'full' },
  { path: 'home', component: HomePage },
  { path: 'login', component: LoginComponent },
  { path: 'myAccount', component: MyAccount},
  { path: 'shoppingCart', component: ShoppingCart},
  { path: 'itemsList', component: ItemsList},
  { path: 'itemsList/:type', component: ItemsList}
];
