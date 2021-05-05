import { AbstractControl, ValidatorFn } from '@angular/forms';

/** A hero's name can't match the given regular expression */
export function passwordValidator(): ValidatorFn {
  return (control: AbstractControl): {[key: string]: any} | null => {
    const forbidden = /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[^\da-zA-Z]).{8,}$/.test(control.value);
    return forbidden ? {forbiddenName: {value: control.value}} : null;
  };
}
