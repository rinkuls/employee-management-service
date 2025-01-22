import { Routes } from '@angular/router';
import { LoginComponent } from './login/login.component';
import { WelcomeComponent } from './welcome/welcome.component';

// Define the routes for the application
export const appRoutes: Routes = [
  { path: '', redirectTo: 'login', pathMatch: 'full' }, // Redirect root to login page
  { path: 'login', component: LoginComponent },         // Route to LoginComponent
  { path: 'welcome', component: WelcomeComponent },     // Route to WelcomeComponent
  { path: '**', redirectTo: 'login' },                  // Wildcard route for unknown paths
];
