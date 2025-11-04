import { Component, Input, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ProductService, ProductMovement } from '../../services/product.service';

@Component({
  selector: 'app-product-movements',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './product-movements.component.html',
  styleUrls: ['./product-movements.component.css']
})
export class ProductMovementsComponent implements OnInit {
  @Input() productId!: number;
  @Input() productName!: string;
  
  movements: ProductMovement[] = [];
  loading: boolean = true;
  error: string | null = null;

  constructor(private productService: ProductService) {}

  ngOnInit(): void {
    if (this.productId) {
      this.loadMovements();
    }
  }

  loadMovements(): void {
    this.loading = true;
    this.error = null;
    
    this.productService.getProductMovements(this.productId).subscribe({
      next: (movements) => {
        this.movements = movements;
        this.loading = false;
      },
      error: (error) => {
        console.error('Error loading movements:', error);
        this.error = 'Failed to load movements';
        this.loading = false;
      }
    });
  }

  getMovementTypeClass(movementType: string): string {
    return movementType === 'IN' ? 'movement-in' : 'movement-out';
  }

  getMovementTypeIcon(movementType: string): string {
    return movementType === 'IN' ? 'arrow-up' : 'arrow-down';
  }

  getMovementTypeText(movementType: string): string {
    return movementType === 'IN' ? 'Stock In' : 'Stock Out';
  }

  formatDate(dateString: string): string {
    const date = new Date(dateString);
    return date.toLocaleDateString('en-US', {
      year: 'numeric',
      month: 'short',
      day: 'numeric',
      hour: '2-digit',
      minute: '2-digit'
    });
  }

  getCauseClass(cause: string): string {
    const causeClasses: { [key: string]: string } = {
      'PURCHASE': 'cause-purchase',
      'SALE': 'cause-sale',
      'RETURN': 'cause-return',
      'ADJUSTMENT': 'cause-adjustment',
      'DAMAGE': 'cause-damage',
      'THEFT': 'cause-theft'
    };
    return causeClasses[cause] || 'cause-default';
  }

  getCauseText(cause: string): string {
    const causeTexts: { [key: string]: string } = {
      'PURCHASE': 'Purchase',
      'SALE': 'Sale',
      'RETURN': 'Return',
      'ADJUSTMENT': 'Adjustment',
      'DAMAGE': 'Damage',
      'THEFT': 'Theft'
    };
    return causeTexts[cause] || cause;
  }
}
