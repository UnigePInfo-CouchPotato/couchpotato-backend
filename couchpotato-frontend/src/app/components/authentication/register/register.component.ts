import { Component } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { AuthenticationService } from 'src/app/services/authentication.service';
import { passwordValidator } from 'src/app/utils/validators';

/** Component used to display a registration form. */
@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['../authentication.sub-components.scss']
})
export class RegisterComponent {
  /** Structure grouping all of the content of the reactive form. */
  formData: FormGroup;

  /** Creates an instance of RegisterComponent.
   *
   * @param authenticationService The service that deals with authenticating and storing user existence.
   */
  constructor(private authenticationService: AuthenticationService) {
    this.formData = new FormGroup({
      username: new FormControl('', [
        Validators.required,
        Validators.minLength(4)
      ]),
      password: new FormControl('', [
        Validators.required,
        Validators.minLength(8),
        passwordValidator()
      ]),
      email: new FormControl('', [
        Validators.required,
        Validators.email
      ])
    });
  }

  /** Function called when the user presses the register button (validation is handled by the FormControl),
   * which tells the authentication service to attempt to log the user in.
   */
  onSubmit(): void {
    this.authenticationService.attemptRegistration(
          this.formData.get('username')?.value,
          this.formData.get('email')?.value,
          this.formData.get('password')?.value
    );
  }

}
