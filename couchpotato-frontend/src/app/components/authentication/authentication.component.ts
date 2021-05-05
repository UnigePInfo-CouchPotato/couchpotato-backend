import { Component } from '@angular/core';

/** Component used to display anything from login to registration forms. */
@Component({
  selector: 'app-authentication',
  templateUrl: './authentication.component.html',
  styleUrls: ['./authentication.component.scss']
})
export class AuthenticationComponent {
  /** Whether or not the currently selected mode is to login. */
  loginSelected: boolean = true;

  /** Toggle the mode to the one defined by the current button. */
  set selected(isLogin: boolean) {
    this.loginSelected = isLogin;
  }
}
