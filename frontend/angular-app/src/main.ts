import { provideHttpClient, withInterceptors } from '@angular/common/http';
import { bootstrapApplication } from '@angular/platform-browser';
import { AppComponent } from './app/app.component';
import { authInterceptor } from './app/auth.interceptor';
import { correlationIdInterceptor } from './app/correlation-id.interceptor';

bootstrapApplication(AppComponent, {
  providers: [provideHttpClient(withInterceptors([correlationIdInterceptor, authInterceptor]))],
}).catch((error: unknown) => console.error(error));
