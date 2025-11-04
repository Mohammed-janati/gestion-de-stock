import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ProductService, Product } from '../../services/product.service';
import { ProductMovementsComponent } from '../product-movements/product-movements.component';

@Component({
  selector: 'app-product-list',
  standalone: true,
  imports: [CommonModule, FormsModule, ProductMovementsComponent],
  templateUrl: './product-list.component.html',
  styleUrls: ['./product-list.component.css']
})
export class ProductListComponent implements OnInit {
  products: Product[] = [];
  filteredProducts: Product[] = [];
  selectedProduct: Product | null = null;
  showProductForm: boolean = false;
  showMovementModal: boolean = false;
  isEditing: boolean = false;
  searchTerm: string = '';
  stockFilter: string = 'all';
  
  // Form data
  productForm: Product = {
    name: '',
    description: '',
    productCode: '',
    price: 0,
    quantity: 0,
    minStockLevel: 0,
    isActive: true
  };

  // Stock update form
  stockUpdateForm = {
    quantity: 0,
    cause: 'PURCHASE'
  };

  constructor(private productService: ProductService) {}

  ngOnInit(): void {
    this.loadProducts();
  }

  loadProducts(): void {
    this.productService.getAllProducts().subscribe({
      next: (products) => {
        this.products = products;
        this.applyFilters();
      },
      error: (error) => {
        console.error('Error loading products:', error);
      }
    });
  }

  applyFilters(): void {
    let filtered = [...this.products];

    // Apply search filter
    if (this.searchTerm) {
      filtered = filtered.filter(product => 
        product.name.toLowerCase().includes(this.searchTerm.toLowerCase()) ||
        product.productCode.toLowerCase().includes(this.searchTerm.toLowerCase()) ||
        (product.description && product.description.toLowerCase().includes(this.searchTerm.toLowerCase()))
      );
    }

    // Apply stock filter
    switch (this.stockFilter) {
      case 'low':
        filtered = filtered.filter(product => product.quantity <= product.minStockLevel);
        break;
      case 'out':
        filtered = filtered.filter(product => product.quantity === 0);
        break;
      case 'active':
        filtered = filtered.filter(product => product.isActive);
        break;
      case 'inactive':
        filtered = filtered.filter(product => !product.isActive);
        break;
    }

    this.filteredProducts = filtered;
  }

  onSearchChange(): void {
    this.applyFilters();
  }

  onStockFilterChange(): void {
    this.applyFilters();
  }

  openAddProduct(): void {
    this.isEditing = false;
    this.productForm = {
      name: '',
      description: '',
      productCode: '',
      price: 0,
      quantity: 0,
      minStockLevel: 0,
      isActive: true
    };
    this.showProductForm = true;
  }

  openEditProduct(product: Product): void {
    this.isEditing = true;
    this.productForm = { ...product };
    this.showProductForm = true;
  }

  saveProduct(): void {
    if (this.isEditing && this.productForm.id) {
      this.productService.updateProduct(this.productForm.id, this.productForm).subscribe({
        next: () => {
          this.loadProducts();
          this.showProductForm = false;
        },
        error: (error) => {
          console.error('Error updating product:', error);
        }
      });
    } else {
      this.productService.createProduct(this.productForm).subscribe({
        next: () => {
          this.loadProducts();
          this.showProductForm = false;
        },
        error: (error) => {
          console.error('Error creating product:', error);
        }
      });
    }
  }

  deleteProduct(product: Product): void {
    if (confirm(`Are you sure you want to delete "${product.name}"?`)) {
      if (product.id) {
        this.productService.deleteProduct(product.id).subscribe({
          next: () => {
            this.loadProducts();
          },
          error: (error) => {
            console.error('Error deleting product:', error);
          }
        });
      }
    }
  }

  openMovementModal(product: Product): void {
    this.selectedProduct = product;
    this.showMovementModal = true;
  }

  closeMovementModal(): void {
    this.selectedProduct = null;
    this.showMovementModal = false;
    this.stockUpdateForm = { quantity: 0, cause: 'PURCHASE' };
  }

  updateStock(): void {
    if (this.selectedProduct && this.selectedProduct.id) {
      this.productService.updateStock(
        this.selectedProduct.id, 
        this.stockUpdateForm.quantity, 
        this.stockUpdateForm.cause
      ).subscribe({
        next: () => {
          this.loadProducts();
          this.closeMovementModal();
        },
        error: (error) => {
          console.error('Error updating stock:', error);
        }
      });
    }
  }

  removeStock(): void {
    if (this.selectedProduct && this.selectedProduct.id) {
      this.productService.removeStock(
        this.selectedProduct.id, 
        this.stockUpdateForm.quantity, 
        this.stockUpdateForm.cause
      ).subscribe({
        next: () => {
          this.loadProducts();
          this.closeMovementModal();
        },
        error: (error) => {
          console.error('Error removing stock:', error);
        }
      });
    }
  }

  getStockStatusClass(quantity: number, minStock: number): string {
    if (quantity === 0) return 'stock-out';
    if (quantity <= minStock) return 'stock-low';
    return 'stock-ok';
  }

  getStockStatusText(quantity: number, minStock: number): string {
    if (quantity === 0) return 'Out of Stock';
    if (quantity <= minStock) return 'Low Stock';
    return 'In Stock';
  }
}
