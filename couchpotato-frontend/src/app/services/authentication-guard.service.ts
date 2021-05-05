import { Injectable } from '@angular/core';
import { CanActivate, Router } from '@angular/router';
import { AuthenticationService } from './authentication.service';

/** Serves as a way to guard against accessing certain pages without being logged in. */
@Injectable({
  providedIn: 'root'
})
export class AuthenticationGuardService implements CanActivate {

  /** Creates an instance of AuthenticationGuardService.
   *
   * @param authService The authentication service used to determine whether or not the user is authenticated.
   * @param router The service used to change what the user is viewing.
   */
  constructor(private authService: AuthenticationService, private router: Router) { }

  /** Whether the route can be activated when the guard is triggered. */
  canActivate(): boolean {
    if (!this.authService.isAuthenticated) {
      this.router.navigate(['authenticate']);
      return false;
    }
    return true;
  }
}
