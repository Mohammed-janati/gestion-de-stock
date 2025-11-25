import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface Product {
  id?: number;
  name: string;
  description?: string;
  productCode: string;
  price: number;
  quantity: number;
  minStockLevel: number;
  categoryId?: number;
  isActive?: boolean;
}

export interface ProductStats {
  totalProducts: number;
  activeProducts: number;
  lowStockProducts: number;
  outOfStockProducts: number;
}

export interface ProductMovement {
  id?: number;
  productId: number;
  movementType: 'IN' | 'OUT';
  quantity: number;
  cause: string;
  timestamp?: string;
  product?: Product;
}

@Injectable({
  providedIn: 'root'
})
export class ProductService {
  private readonly API_URL = (window as any).__env__.API_URL+'/api';

  constructor(private http: HttpClient) {}

  private getHeaders(): HttpHeaders {
    const token = localStorage.getItem('auth_token');
    return new HttpHeaders({
      'Content-Type': 'application/json',
      'Authorization': `Bearer ${token}`
    });
  }

  // Product CRUD Operations
  getAllProducts(): Observable<Product[]> {
    return this.http.get<Product[]>(`${this.API_URL}/products`, { headers: this.getHeaders() });
  }

  getProductById(id: number): Observable<Product> {
    return this.http.get<Product>(`${this.API_URL}/products/${id}`, { headers: this.getHeaders() });
  }

  createProduct(product: Product): Observable<string> {
    return this.http.post<string>(`${this.API_URL}/products`, product, {
      headers: this.getHeaders(),
      responseType: 'text' as 'json'
    });
  }

  updateProduct(id: number, product: Product): Observable<string> {
    return this.http.put<string>(`${this.API_URL}/products/${id}`, product, {
      headers: this.getHeaders(),
      responseType: 'text' as 'json'
    });
  }

  deleteProduct(id: number): Observable<string> {
    return this.http.delete<string>(`${this.API_URL}/products/${id}`, {
      headers: this.getHeaders(),
      responseType: 'text' as 'json'
    });
  }

  // Product Statistics
  getProductStats(): Observable<ProductStats> {
    return this.http.get<ProductStats>(`${this.API_URL}/products/stats`, { headers: this.getHeaders() });
  }

  getLowStockProducts(): Observable<Product[]> {
    return this.http.get<Product[]>(`${this.API_URL}/products/low-stock`, { headers: this.getHeaders() });
  }

  getOutOfStockProducts(): Observable<Product[]> {
    return this.http.get<Product[]>(`${this.API_URL}/products/out-of-stock`, { headers: this.getHeaders() });
  }

  // Stock Management
  updateStock(id: number, quantity: number, cause: string): Observable<string> {
    return this.http.put<string>(`${this.API_URL}/products/${id}/stock?quantity=${quantity}`, { cause }, {
      headers: this.getHeaders(),
      responseType: 'text' as 'json'
    });
  }

  removeStock(id: number, amount: number, cause: string): Observable<string> {
    return this.http.post<string>(`${this.API_URL}/products/${id}/remove-stock?amount=${amount}`, { cause }, {
      headers: this.getHeaders(),
      responseType: 'text' as 'json'
    });
  }

  // Product Movements
  getProductMovements(productId: number): Observable<ProductMovement[]> {
    return this.http.get<ProductMovement[]>(`${this.API_URL}/product-movements/product/${productId}`, { headers: this.getHeaders() });
  }

  getAllMovements(): Observable<ProductMovement[]> {
    return this.http.get<ProductMovement[]>(`${this.API_URL}/product-movements`, { headers: this.getHeaders() });
  }

  getRecentMovements(productId: number, limit: number = 10): Observable<ProductMovement[]> {
    return this.http.get<ProductMovement[]>(`${this.API_URL}/product-movements/product/${productId}/recent?limit=${limit}`, { headers: this.getHeaders() });
  }
}
