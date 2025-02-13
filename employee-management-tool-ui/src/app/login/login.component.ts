import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { FormsModule } from '@angular/forms';  
import { AuthService } from '../services/auth.service';
import { HttpClient, HttpHeaders } from '@angular/common/http';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [FormsModule], 
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})
export class LoginComponent {
  username: string = '';
  password: string = '';

  constructor(private authService: AuthService, private http: HttpClient, private router: Router) {}

  validateUsername() {
    if (!/^[a-zA-Z]+$/.test(this.username)) {
      alert('Username should only contain alphabets.');
      this.username = ''; 
    }
  }

  validatePassword() {
    if (/\s/.test(this.password)) {
      alert('Password should not contain spaces.');
      this.password = ''; 
    }
  }

  onSubmit() {
    this.authService.login(this.username, this.password).subscribe({
      next: (tokens) => {
        if (tokens.access_token) {
          this.fetchUserRole(tokens.access_token);
        } else {
          alert('Login failed!');
        }
      },
      error: () => {
        alert('Invalid credentials. Please try again.');
      }
    });
  }

  fetchUserRole(token: string) {
    const headers = new HttpHeaders({
      Authorization: `Bearer ${token}`
    });

    this.http.post<{ role: string; username: string }>(
      'http://localhost:8071/api/v1/user/fetchRole', 
      {},  
      { headers }
    )
    .subscribe({
      next: (response) => {
        if (response.role) {
          localStorage.setItem('userRole', response.role);
          localStorage.setItem('userName', response.username);
          this.router.navigate(['/welcome']);
        } else {
          alert('Failed to fetch user role!');
        }
      },
      error: () => {
        alert('Error fetching user role.');
      }
    });
  }
}
