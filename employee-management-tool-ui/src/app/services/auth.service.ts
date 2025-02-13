import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders, HttpErrorResponse } from '@angular/common/http';
import { Router } from '@angular/router';
import { catchError, tap, throwError, BehaviorSubject, switchMap, Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';

const KEYCLOAK_URL = 'http://localhost:8080';
const REALM = 'employee-management';
const CLIENT_ID = 'employee-management-ui';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private isRefreshing = false;
  private refreshTokenSubject: BehaviorSubject<string | null> = new BehaviorSubject<string | null>(null);

  constructor(private http: HttpClient, private router: Router) {}

  login(username: string, password: string) {
    const body = new URLSearchParams();
    body.set('client_id', CLIENT_ID);
    body.set('grant_type', 'password');
    body.set('username', username);
    body.set('password', password);

    const headers = new HttpHeaders({ 'Content-Type': 'application/x-www-form-urlencoded' });

    return this.http.post<{ access_token: string; refresh_token: string }>(
      `${KEYCLOAK_URL}/realms/${REALM}/protocol/openid-connect/token`,
      body.toString(),
      { headers }
    ).pipe(
      tap(response => {
        if (response.access_token && response.refresh_token) {
          this.storeTokens(response.access_token, response.refresh_token);
        }
      }),
      catchError(error => this.handleError(error))
    );
  }

  refreshToken(): Observable<string> {
    if (this.isRefreshing) {
      return this.refreshTokenSubject.asObservable().pipe(
        filter(token => token !== null),
        map(token => token as string)
      );
    }
  
    this.isRefreshing = true;
  
    const refreshToken = localStorage.getItem('refreshToken');
    if (!refreshToken) {
      this.logout();
      return throwError(() => new Error('No refresh token available'));
    }
  
    const body = new URLSearchParams();
    body.set('client_id', CLIENT_ID);
    body.set('grant_type', 'refresh_token');
    body.set('refresh_token', refreshToken);
  
    return this.http.post<{ access_token: string; refresh_token: string }>(
      `${KEYCLOAK_URL}/realms/${REALM}/protocol/openid-connect/token`,
      body.toString(),
      { headers: new HttpHeaders({ 'Content-Type': 'application/x-www-form-urlencoded' }) }
    ).pipe(
      tap(response => {
        if (response.access_token && response.refresh_token) {
          this.storeTokens(response.access_token, response.refresh_token);
          this.refreshTokenSubject.next(response.access_token);
        }
        this.isRefreshing = false;
      }),
      switchMap(() => this.refreshTokenSubject.asObservable()),
      filter(token => token !== null),
      map(token => token as string),
      catchError(error => {
        this.isRefreshing = false;
        this.logout();
        return throwError(() => this.handleError(error));
      })
    );
  }
  
  getAuthHeaders(): Observable<HttpHeaders> {
    const token = localStorage.getItem('jwtToken');

    if (!token) {
      return this.refreshToken().pipe(
        switchMap(newToken => {
          return of(new HttpHeaders({
            Authorization: `Bearer ${newToken}`,
            'Content-Type': 'application/json'
          }));
        })
      );
    }

    return of(new HttpHeaders({
      Authorization: `Bearer ${token}`,
      'Content-Type': 'application/json'
    }));
  }

  storeTokens(accessToken: string, refreshToken: string) {
    localStorage.setItem('jwtToken', accessToken);
    localStorage.setItem('refreshToken', refreshToken);
  }

  logout() {
    localStorage.removeItem('jwtToken');
    localStorage.removeItem('refreshToken');
    localStorage.removeItem('userRole');
    localStorage.removeItem('userName');
    this.router.navigate(['/login']);
  }

  private handleError(error: HttpErrorResponse) {
    console.error('Auth Error:', error);
    return throwError(() => new Error(error.message));
  }
}
