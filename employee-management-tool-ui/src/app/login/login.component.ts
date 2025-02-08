import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { catchError, of, switchMap, tap } from 'rxjs';
import { environment } from '../../environments/environment';  



const API_URL_AUTH = environment.API_URL_AUTH;
const API_URL_EMPLOYEE = environment.API_URL_EMPLOYEE;
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
  empId: string = '';
  constructor(private http: HttpClient, private router: Router) { }
  validateEmpId(): void {
    this.empId = this.empId.replace(/\s+/g, ''); // Remove spaces
    if (!/^\d+$/.test(this.empId)) {
      alert('Employee ID must only contain numeric values.');
      this.empId = this.empId.replace(/\D/g, ''); // Remove non-numeric characters
    }
  }

  validateUsername(): void {
    if (!/^[a-zA-Z]*$/.test(this.username)) {
      alert('Username must only contain alphabets.');
      this.username = this.username.replace(/[^a-zA-Z]/g, ''); // Remove invalid characters
    }
  }

  validatePassword(): void {
    if (this.password.includes(' ')) {
      alert('Password must not contain spaces.');
      this.password = this.password.replace(/\s+/g, ''); // Remove spaces
    }
  }

  onSubmit() {
    const numericEmpId = Number(this.empId); // Convert to number before sending
    if (isNaN(numericEmpId)) {
      alert('Invalid Employee ID.');
      return;
    }
    console.log('Request Payload:', {
      username: this.username,
      password: this.password,
      empId: this.empId,
    });

   

    this.http.post<{ token: string }>(`${API_URL_AUTH}/api/v1/auth/login`, {
      username: this.username,
      password: this.password,
      empId: this.empId
    }, {
      headers: { 'Content-Type': 'application/json' }
    }).pipe(
      tap((response) => {
        console.log('Server Response:', response);
        if (response.token) {
          localStorage.setItem('jwtToken', response.token);

        }
      }),
      switchMap((response) => {
        if (!response.token) {
          throw new Error('No token received');
        }

        const token = response.token;

        const headers = new HttpHeaders({
          Authorization: `Bearer ${token}`
        });

        return this.http.post<{ role: string, empId: number, username: string }>(
          `${API_URL_EMPLOYEE}/api/v1/user/fetchRole`,
          {}, // Empty body
          { headers }
        );
      }),


      // Step 3: Process the role from the fetchRole API response
      tap((userDetails) => {
        const role = userDetails.role;

        console.log('User Details:', userDetails);
        localStorage.setItem('role', userDetails.role);
        localStorage.setItem('name', userDetails.username);

        if (role === 'ADMIN') {

          this.router.navigate(['/welcome']);
        } else if (role === 'USER') {

          this.router.navigate(['/welcome']);

        } else {
          alert('Unauthorized role');
        }
      }),
      catchError((error) => {
        console.error('Error:', error);
        alert('An error occurred. Please check the console for details.');
        return of(null);
      })
    ).subscribe();
  }

}
