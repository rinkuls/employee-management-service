import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { environment } from '../../environments/environment';
import { AuthService } from '../services/auth.service';
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

  fetchSuccessMessage: boolean = false;
  fetchErrorMessage: boolean = false;
  submitSuccessMessage: boolean = false;
  submitErrorMessage: boolean = false;

  private messageTimeout: any;

  user = {
    username: '',
    empId: null,
    role: 'USER',
  };
  empId: number | null = null;
  employee = {
    name: '',
    empId: null,
    email: '',
    phoneNumber: '',
    address: '',
    married: false,
    extraMartialAffair: false,
    dreamWish: '',
    natureBehavior: '',
    profilePicture: null,
    kids: [] as { name: string; age: number | null; gender: string; profession: string }[], 
    spouse: { name: '', age: null, gender: '', currentOccupation: '' },
    professionalDetails: {
      currentCompany: '',
      currentDesignation: '',
      currentSalary: null,
      currentLocation: '',
    },
    pastEmployments: [] as { companyName: string; designation: string; salary: number | null }[],
  };
     
  hasKids: boolean = false;

  private readonly API_URL_REGISTER = `${API_URL_EMPLOYEE}/api/v1/employee/addEmployee`;

  constructor(
    private router: Router, 
    private http: HttpClient, 
    private authService: AuthService, 
    private cdr: ChangeDetectorRef
  ) {}

  ngOnInit(): void {
    console.log('Fetching stored values from localStorage...');
    console.log('Stored userRole:', localStorage.getItem('userRole'));
    console.log('Stored userName:', localStorage.getItem('userName'));

    this.role = localStorage.getItem('userRole') || '';
    this.name = localStorage.getItem('userName') || '';

    // Force UI update to reflect values
    this.cdr.detectChanges();
  }

  logout(): void {
    localStorage.removeItem('userRole');
    localStorage.removeItem('userName');
    localStorage.removeItem('jwtToken');
    this.router.navigate(['/login']);
  }

  showAddUserForm() {
    this.showForm = !this.showForm;
  }

  showEmployeeSearchForm() {
    this.showEmployeeSearch = !this.showEmployeeSearch;
    this.fetchSuccessMessage = false;
    this.fetchErrorMessage = false;
  }

  fetchEmployeeDetails() {
    this.authService.getAuthHeaders().subscribe(headers => {
      this.http.get(`${API_URL_EMPLOYEE}/api/v1/pdf/${this.empId}`, {
        headers,
        responseType: 'blob',
      })
      .subscribe({
        next: (response: Blob) => {
          const url = window.URL.createObjectURL(response);
          const a = document.createElement('a');
          a.href = url;
          a.download = `employee_${this.empId}.pdf`;
          a.click();

          this.fetchSuccessMessage = true;
          this.fetchErrorMessage = false;

          setTimeout(() => {
            this.fetchSuccessMessage = true;
            this.showEmployeeSearch = false; // Hide form after success
            this.empId = null; // Reset input field
            setTimeout(() => {
              this.fetchSuccessMessage = false; // Hide success message after a delay
            }, 3000);
          }, 1000);
        },
        error: () => {
          this.fetchSuccessMessage = false;
          this.fetchErrorMessage = true;

          setTimeout(() => {
            this.fetchErrorMessage = false;
          }, 3000);
        },
      });
    });
  }

  onSubmit() {
    this.authService.getAuthHeaders().subscribe(headers => {
      this.http.post(`${API_URL_EMPLOYEE}/api/v1/employee/addEmployee`, this.employee, { headers, responseType: 'text' })
      .subscribe({
        next: () => {
          this.submitSuccessMessage = true;
          this.showForm = false;

          setTimeout(() => {
            this.submitSuccessMessage = false;
            this.resetForm();
          }, 3000);
        },
        error: () => {
          this.submitSuccessMessage = false;
          this.submitErrorMessage = true;

          setTimeout(() => {
            this.submitErrorMessage = false;
          }, 3000);
        },
      });
    });
  }

  resetForm() {
    this.employee = {
      name: '',
      empId: null,
      email: '',
      phoneNumber: '',
      address: '',
      married: false,
      extraMartialAffair: false,
      dreamWish: '',
      natureBehavior: '',
      profilePicture: null,
      kids: [],
      spouse: { name: '', age: null, gender: '', currentOccupation: '' },
      professionalDetails: {
        currentCompany: '',
        currentDesignation: '',
        currentSalary: null,
        currentLocation: '',
      },
      pastEmployments: []
    };
    this.showForm = false;
  }
  
  toggleSpouseDetails(married: boolean) {
    if (!married) {
      this.employee.spouse = { name: '', age: null, gender: '', currentOccupation: '' };
    }
  }
  

      // Toggle Kids Section
  toggleKids(hasKids: boolean) {
    this.hasKids = hasKids;
    if (!hasKids) {
      this.employee.kids = [];
    }
  }

  addKid() {
    this.employee.kids.push({ name: '', age: null, gender: '', profession: '' });
  }
  
  
  
    // Remove a Kid Entry
    removeKid(index: number) {
      this.employee.kids.splice(index, 1);
    }

    addPastEmployment() {
      this.employee.pastEmployments.push({ companyName: '', designation: '', salary: null });
    }
    
    
// Remove a Past Employment Entry
removePastEmployment(index: number) {
  this.employee.pastEmployments.splice(index, 1);
}
}
