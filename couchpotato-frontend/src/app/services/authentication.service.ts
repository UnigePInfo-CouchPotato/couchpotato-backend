import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';

/** Service used to authenticate users. */
@Injectable({
  providedIn: 'root'
})
export class AuthenticationService {
  /** Whether the user is authenticated. */
  private userAuthenticated: boolean = true;

  /** Creates an instance of AuthenticationService.
   *
   * @param http The HTTP client to make requests.
   */
  constructor(private http: HttpClient) { }

  /** Retrieve whether the user is authenticated. */
  get isAuthenticated(): boolean {
    return this.userAuthenticated;
  }

  /** Attempts to register a new user. */
  attemptRegistration(username: string, email: string, password: string) {
    console.log(`Registration start with username (${username}) and password (${password}) and email (${email})`);
    // this.http.post

    return;
  }

  /** Attempts to login a user. */
  attemptLogin(username: string, password: string) {
    console.log(`Login start with username (${username}) and password (${password})`);
    // this.http.get(``);

    return;
  }
}
