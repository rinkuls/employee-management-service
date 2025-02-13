import { bootstrapApplication } from '@angular/platform-browser';
import { AppComponent } from './app/app.component';
import { importProvidersFrom, Provider } from '@angular/core';
import { RouterModule } from '@angular/router';
import { appRoutes } from './app/app.routes';
import { provideHttpClient, withInterceptorsFromDi, HTTP_INTERCEPTORS } from '@angular/common/http';
import { AuthInterceptor } from './app/services/auth.interceptor';

// Define interceptor provider
const httpInterceptorProvider: Provider = {
  provide: HTTP_INTERCEPTORS,
  useClass: AuthInterceptor,
  multi: true // Allow multiple interceptors if needed
};

// Bootstrap the application with required providers
bootstrapApplication(AppComponent, {
  providers: [
    importProvidersFrom(RouterModule.forRoot(appRoutes, { useHash: false })), // Properly wrap RouterModule
    provideHttpClient(withInterceptorsFromDi()), // Enable HTTP client with DI Interceptors
    httpInterceptorProvider // Register AuthInterceptor correctly
  ]
}).catch(err => console.error(err));
