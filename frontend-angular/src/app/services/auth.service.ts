import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { BehaviorSubject, Observable, tap } from 'rxjs';
import { Router } from '@angular/router';
import { environment } from '../environment';
import { AuthResponse, LoginRequest, RegisterRequest, UserResponse } from '../models/auth.model';
import { jwtDecode } from 'jwt-decode';

interface TokenPayload {
  exp: number;
  username: string;
  iat: number;
}

@Injectable({ providedIn: 'root' })
export class AuthService {
  private currentUserSubject = new BehaviorSubject<UserResponse | null>(null);
  currentUser$ = this.currentUserSubject.asObservable();

  constructor(private http: HttpClient, private router: Router) {
    const stored = localStorage.getItem('user');
    if (stored) {
      this.currentUserSubject.next(JSON.parse(stored));
    }
  }

  login(request: LoginRequest): Observable<AuthResponse> {
    return this.http.post<AuthResponse>(`${environment.apiUrl}/auth/login`, request).pipe(
      tap(response => this.handleAuth(response))
    );
  }

  register(request: RegisterRequest): Observable<AuthResponse> {
    return this.http.post<AuthResponse>(`${environment.apiUrl}/auth/register`, request).pipe(
      tap(response => this.handleAuth(response))
    );
  }

  refreshToken(): Observable<AuthResponse> {
    const refreshToken = localStorage.getItem('refresh_token');
    console.log('Attempting to refresh token. Refresh token exists:', !!refreshToken);
    
    if (!refreshToken) {
      console.error('No refresh token available');
      throw new Error('No refresh token available');
    }
    
    return this.http.post<AuthResponse>(`${environment.apiUrl}/auth/refresh`, {
      refresh_token: refreshToken
    }).pipe(
      tap(response => {
        console.log('Refresh token successful, new token expires at:', response.expires_in);
        this.handleAuth(response);
      })
    );
  }

  logout(): void {
    localStorage.removeItem('access_token');
    localStorage.removeItem('refresh_token');
    localStorage.removeItem('user');
    this.currentUserSubject.next(null);
    this.router.navigate(['/login']);
  }

  getToken(): string | null {
    return localStorage.getItem('access_token');
  }

  getRefreshToken(): string | null {
    return localStorage.getItem('refresh_token');
  }

  isLoggedIn(): boolean {
    const token = this.getToken();
    return !!token && !this.isTokenExpired();
  }

  isTokenExpired(): boolean {
    const token = this.getToken();
    if (!token) {
      console.log('isTokenExpired: No token');
      return true;
    }
    
    try {
      const decoded = jwtDecode<TokenPayload>(token);
      const expiresAt = decoded.exp * 1000; // Convert to milliseconds
      const now = Date.now();
      const isExpired = now >= expiresAt;
      
      console.log('isTokenExpired check:', {
        expiresAt: new Date(expiresAt).toISOString(),
        now: new Date(now).toISOString(),
        isExpired,
        diff_ms: expiresAt - now
      });
      
      return isExpired;
    } catch (error) {
      console.error('Error decoding token:', error);
      return true;
    }
  }

  getTokenExpirationDate(): Date | null {
    const token = this.getToken();
    if (!token) return null;
    
    try {
      const decoded = jwtDecode<TokenPayload>(token);
      return new Date(decoded.exp * 1000);
    } catch (error) {
      return null;
    }
  }

  get currentUser(): UserResponse | null {
    return this.currentUserSubject.value;
  }

  private handleAuth(response: AuthResponse): void {
    console.log('handleAuth called with response:', {
      access_token: response.access_token?.substring(0, 20) + '...',
      refresh_token: response.refresh_token?.substring(0, 20) + '...',
      expires_in: response.expires_in,
      user: response.user
    });
    
    localStorage.setItem('access_token', response.access_token);
    localStorage.setItem('refresh_token', response.refresh_token);
    localStorage.setItem('user', JSON.stringify(response.user));
    this.currentUserSubject.next(response.user);
    
    console.log('Auth tokens saved to localStorage');
    console.log('Current localStorage keys:', Object.keys(localStorage));
  }
}
