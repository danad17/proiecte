import {ItemModel} from './itemModel';

export class MenuCategory {
  type!: string;
  open!: boolean;
  subtypes!: { name: string; items: ItemModel[] }[];
}
