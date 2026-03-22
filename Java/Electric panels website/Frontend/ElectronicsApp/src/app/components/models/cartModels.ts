export interface CartItemDTO {
  cartItemId: number;
  itemId: number;
  name: string;
  description: string;
  price: number;
  image: string;
  brand: string;
  type: string;
  subtype: string;
  category: string;
  stockAvailable: number;
  quantity: number;
  subtotal: number;
}

export interface CartResponseDTO {
  cartId: number;
  userId: number;
  items: CartItemDTO[];
  total: number;
  totalItemCount: number;
}

export interface AddToCartRequest {
  itemId: number;
  quantity: number;
}

export interface UpdateQuantityRequest {
  quantity: number;
}
