import { Component } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { AuthenticationService } from 'src/app/services/authentication.service';


/** Component used to display a login form. */
@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['../authentication.sub-components.scss']
})
export class LoginComponent {
  /** Structure grouping all of the content of the reactive form. */
  formData: FormGroup;

  /** Creates an instance of LoginComponent.
   *
   * @param authenticationService The service that deals with authenticating and storing user existence.
   */
  constructor(private authenticationService: AuthenticationService) {
    this.formData = new FormGroup({
      username: new FormControl('', [
        Validators.required,
        Validators.minLength(1)
      ]),
      password: new FormControl('', [
        Validators.required,
        Validators.minLength(1),
      ])
    });
  }

  /** Function called when the user presses the login button (validation is handled by the FormControl),
   * which tells the authentication service to attempt to log the user in.
   */
  onSubmit(): void {
    this.authenticationService.attemptLogin(this.formData.get('username')?.value, this.formData.get('password')?.value);
  }
}
