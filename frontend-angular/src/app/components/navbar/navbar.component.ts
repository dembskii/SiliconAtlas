import { Component, OnInit, OnDestroy } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink, RouterLinkActive } from '@angular/router';
import { AuthService } from '../../services/auth.service';
import { Subject, interval } from 'rxjs';
import { takeUntil } from 'rxjs/operators';

@Component({
  selector: 'app-navbar',
  standalone: true,
  imports: [CommonModule, RouterLink, RouterLinkActive],
  templateUrl: './navbar.component.html',
  styleUrl: './navbar.component.scss'
})
export class NavbarComponent implements OnInit, OnDestroy {
  menuOpen = false;
  tokenExpiresAt: Date | null = null;
  isTokenExpiring = false;
  private destroy$ = new Subject<void>();

  constructor(public authService: AuthService) {}

  ngOnInit(): void {
    this.updateTokenStatus();
    // Check token status every minute
    interval(60000)
      .pipe(takeUntil(this.destroy$))
      .subscribe(() => this.updateTokenStatus());
  }

  ngOnDestroy(): void {
    this.destroy$.next();
    this.destroy$.complete();
  }

  private updateTokenStatus(): void {
    this.tokenExpiresAt = this.authService.getTokenExpirationDate();
    
    if (this.tokenExpiresAt) {
      const timeUntilExpiry = this.tokenExpiresAt.getTime() - Date.now();
      // Show warning if token expires in less than 5 minutes
      this.isTokenExpiring = timeUntilExpiry < 5 * 60 * 1000;
    }
  }

  toggleMenu(): void {
    this.menuOpen = !this.menuOpen;
  }

  logout(): void {
    this.authService.logout();
    this.menuOpen = false;
  }
}
