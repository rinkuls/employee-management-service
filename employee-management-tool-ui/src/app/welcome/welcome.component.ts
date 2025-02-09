import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { HttpClient, HttpHeaders } from '@angular/common/http';

import { environment } from '../../environments/environment';  // ✅ Import environment.ts dynamically


const API_URL_EMPLOYEE = environment.API_URL_EMPLOYEE;


@Component({
  selector: 'app-welcome',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './welcome.component.html',
  styleUrls: ['./welcome.component.scss'],
})
export class WelcomeComponent implements OnInit {
  role: string | null = '';
  name: string | null = '';
  showForm: boolean = false;
  showEmployeeSearch: boolean = false;

  // Separate success and error flags
  fetchSuccessMessage: boolean = false;
  fetchErrorMessage: boolean = false;
  submitSuccessMessage: boolean = false;
  submitErrorMessage: boolean = false;

  private messageTimeout: any; // Handle timeout for messages

  user = {
    username: '',
    empId: null,
    role: 'USER',
  };
  empId: number | null = null;  // Changed from employeeName to empId

  private readonly API_URL_REGISTER = `${API_URL_EMPLOYEE}/api/v1/user/addUser`;


  constructor(private router: Router, private http: HttpClient) { }

  ngOnInit(): void {
    this.role = localStorage.getItem('role');
    this.name = localStorage.getItem('name');
  }

  logout(): void {
    localStorage.removeItem('role');
    localStorage.removeItem('name');
    localStorage.removeItem('jwtToken');
    this.router.navigate(['/login']);
  }

  showAddUserForm() {
    this.showForm = !this.showForm;
  }

  showEmployeeSearchForm() {
    this.showEmployeeSearch = !this.showEmployeeSearch;

    // Reset success/error messages for employee search
    this.fetchSuccessMessage = false;
    this.fetchErrorMessage = false;
  }

  fetchEmployeeDetails() {
    const jwtToken = localStorage.getItem('jwtToken');
    if (!jwtToken) {
      alert('Error: Missing authentication token!');
      return;
    }

    const headers = new HttpHeaders({
      Authorization: `Bearer ${jwtToken}`,
    });

    this.http
      .get(`${API_URL_EMPLOYEE}/api/v1/pdf/${this.empId}`, {  // Use empId instead of employeeName
        headers,
        responseType: 'blob',
      })
      .subscribe({
        next: (response: Blob) => {
          const url = window.URL.createObjectURL(response);
          const a = document.createElement('a');
          a.href = url;
          a.download = `employee_${this.empId}.pdf`;  // Use empId in filename
          a.click();

          // Show success message for fetch operation
          this.fetchSuccessMessage = true;
          this.fetchErrorMessage = false;

          setTimeout(() => {
            this.fetchSuccessMessage = false;
          }, 3000);
        },
        error: () => {
          // Show error message for fetch operation
          this.fetchSuccessMessage = false;
          this.fetchErrorMessage = true;

          setTimeout(() => {
            this.fetchErrorMessage = false;
          }, 3000);
        },
      });
  }

  onSubmit() {
    const jwtToken = localStorage.getItem('jwtToken');
    if (!jwtToken) {
      alert('Error: Missing authentication token!');
      return;
    }

    const headers = new HttpHeaders({
      Authorization: `Bearer ${jwtToken}`,
      'Content-Type': 'application/json',
    });

    this.http.post(this.API_URL_REGISTER, this.user, { headers, responseType: 'text' }).subscribe({
      next: () => {
        const userDetails = {
          role: this.user.role,
          empId: this.user.empId,
          name: this.user.username,
        };

        // Show success message for submit operation
        this.submitSuccessMessage = true;
        this.showForm = false;

        clearTimeout(this.messageTimeout);
        this.messageTimeout = setTimeout(() => {
          this.submitSuccessMessage = false;
          this.resetForm();
        }, 10000);
      },
      error: (error) => {
        console.error('Error:', error);

        // Show error message for submit operation
        this.submitSuccessMessage = false;
        this.submitErrorMessage = true;

        if (error.status === 401) {
          alert('Unauthorized! Please log in again.');
        } else if (error.status === 400) {
          alert('Bad Request: Invalid data provided.');
        } else {
          alert('Failed to register user. Please try again.');
        }

        setTimeout(() => {
          this.submitErrorMessage = false;
        }, 3000);
      },
    });
  }

  resetForm() {
    this.user = {
      username: '',
      empId: null,
      role: 'USER',
    };
    this.showForm = false;
  }
}
