import { bootstrapApplication } from '@angular/platform-browser';

import { appConfig } from './app/app.config';
import {App} from './app/app';

fetch('/assets/config.json')
  .then(response => {
    if (!response.ok) throw new Error('Failed to load config.json');
    return response.json();
  })
  .then(config => {
    // Store config globally so you can access it anywhere
    (window as any).__env__ = config;

    // Now bootstrap the Angular app
    return bootstrapApplication(App, appConfig);
  })
  .catch(err => {
    console.error('Error loading configuration:', err);

    // Fallback default values
    (window as any).__env__ = {
      API_URL: 'http://localhost:8080/Auth'
    };

    return bootstrapApplication(App, appConfig);
  });
