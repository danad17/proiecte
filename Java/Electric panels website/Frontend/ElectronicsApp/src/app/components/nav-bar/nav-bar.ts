import {Component, inject} from '@angular/core';
import {NgClass} from '@angular/common';
import {RouterLink} from '@angular/router';
import {Service} from '../services/service';
import {FormsModule} from '@angular/forms';
import {ItemModel} from '../models/itemModel';
import {MenuCategory} from '../models/menuCategory';
import {MatIconButton} from '@angular/material/button';
import {MatTooltip} from '@angular/material/tooltip';
import {MatIconModule} from '@angular/material/icon';
import {MatBadge} from '@angular/material/badge';

@Component({
  selector: 'app-nav-bar',
  imports: [
    RouterLink,
    FormsModule,
    MatIconButton,
    MatIconModule,
    MatTooltip,
    MatBadge,
  ],
  templateUrl: './nav-bar.html',
  styleUrl: './nav-bar.scss',
  standalone: true
})
export class NavBar {

  role: string | null = '';
  isClicked = false;
  searchTerm: string = '';
  selectedCategory: string = '';
  // categories: ItemModel[] = [];
  categories: any[] = [];
  items: ItemModel[] = [];
  menuCategories: MenuCategory[] = [];

  currentCurrency = 'RON';

  constructor(public service: Service) {}

  // changeCurrency(event: Event): void {
  //   const selectElement = event.target as HTMLSelectElement;
  //   const currency = selectElement.value;
  //   this.currencyService.setCurrency(currency);
  // }

  ngOnInit(): void {
    this.role = this.service.getRole();
    this.getItems()
    // this.currentCurrency = this.currencyService.getCurrency();
  }

  onClick(){
    this.isClicked = !this.isClicked;
  }

  search() {
    console.log(this.searchTerm, this.selectedCategory);
  }

  getItems():void{
    this.service.getItems().subscribe({
      next:(data:ItemModel[])=>{
        this.items = data;
        this.buildMenu(data);
        if(this.items.length < 0){
          console.error('Nu exista iteme:');
        }
      },error:error=>{
        console.error('Eroare la obținerea itemelor:', error);
      }});
  }

  buildMenu(items: ItemModel[]) {
    const map = new Map<string, Map<string, ItemModel[]>>();

    items.forEach(item => {
      if (!map.has(item.type)) map.set(item.type, new Map());
      const subMap = map.get(item.type)!;
      if (!subMap.has(item.subtype)) subMap.set(item.subtype, []);
      subMap.get(item.subtype)!.push(item);
    });

    this.menuCategories = Array.from(map.entries()).map(([type, subMap]) => ({
      type,
      open: false,
      subtypes: Array.from(subMap.entries()).map(([name, items]) => ({ name, items }))
    }));
  }
}
