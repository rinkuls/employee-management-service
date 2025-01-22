import { bootstrapApplication } from '@angular/platform-browser';
import { AppComponent } from './app/app.component';
import { importProvidersFrom } from '@angular/core';
import { RouterModule } from '@angular/router';
import { appRoutes } from './app/app.routes';
import { provideHttpClient } from '@angular/common/http';

// Configure the application
export const appConfig = {
  providers: [
    importProvidersFrom(
      RouterModule.forRoot(appRoutes, { useHash: false }) // Hashless routing for modern SPAs
    ),
    provideHttpClient() // Enable HTTP client
  ],
};

// Bootstrap the application
bootstrapApplication(AppComponent, appConfig)
  .catch((err) => console.error(err));
