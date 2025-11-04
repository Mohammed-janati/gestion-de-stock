import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { BehaviorSubject, map, Observable } from 'rxjs';
import { Router } from '@angular/router';

export interface LoginRequest {
  username: string;
  password: string;
}

export interface RegisterRequest {
  username: string;
  password: string;
}

export interface AuthResponse {
  token: string;
}

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private readonly API_URL = (window as any).__env__.API_URL+'/Auth';
  private readonly TOKEN_KEY = 'auth_token';

  private isAuthenticatedSubject = new BehaviorSubject<boolean>(this.hasValidToken());
  public isAuthenticated$ = this.isAuthenticatedSubject.asObservable();

  constructor(
    private http: HttpClient,
    private router: Router
  ) {}

  login(credentials: LoginRequest) {
    return  this.http.post<string>(`${this.API_URL}/login`, credentials, {
      responseType: 'text' as 'json'
    }).pipe(
       map(  (token) => {

                this.setToken(token);
                this.isAuthenticatedSubject.next(true);

    })

  );
  }

  register(userData: RegisterRequest): Observable<string> {
    return this.http.post<string>(`${this.API_URL}/register`, userData, {
      responseType: 'text' as 'json'
    });
  }

  logout(): void {
    localStorage.removeItem(this.TOKEN_KEY);
    this.isAuthenticatedSubject.next(false);
    this.router.navigate(['/login']);
  }

  getToken(): string | null {
    return localStorage.getItem(this.TOKEN_KEY);
  }

  private setToken(token: string): void {
    localStorage.setItem(this.TOKEN_KEY, token);
  }

  private hasValidToken(): boolean {
    const token = this.getToken();
    if (!token) {
      return false;
    }

    try {
      // Basic token validation - check if it's not expired
      const payload = JSON.parse(atob(token.split('.')[1]));
      const currentTime = Date.now() / 1000;
      return payload.exp > currentTime;
    } catch (error) {
      return false;
    }
  }

  isAuthenticated(): boolean {
    return this.hasValidToken();
  }
}
