import { HttpInterceptorFn, HttpErrorResponse } from '@angular/common/http';
import { catchError, throwError } from 'rxjs';

export const errorInterceptor: HttpInterceptorFn = (req, next) => {
  return next(req).pipe(
    catchError((error: HttpErrorResponse) => {
      let message: string;

      switch (error.status) {
        case 0:
          message = 'Server nicht erreichbar. Läuft das Backend?';
          break;
        case 400:
          message = error.error?.message ?? 'Ungültige Anfrage.';
          break;
        case 404:
          message = error.error?.message ?? 'Ressource nicht gefunden.';
          break;
        case 500:
          message = 'Interner Serverfehler.';
          break;
        default:
          message = `Fehler ${error.status}: ${error.message}`;
      }

      return throwError(() => new Error(message));
    })
  );
};
