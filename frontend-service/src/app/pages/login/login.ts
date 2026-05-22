import { Component } from '@angular/core';
import { LoginSectionBanner } from '@components/login/login-section-banner/login-section-banner';
import { LoginSectionForm } from '@components/login/login-section-form/login-section-form';

@Component({
  standalone: true,
  selector: 'app-login',
  imports: [LoginSectionBanner, LoginSectionForm],
  templateUrl: './login.html',
})
export class Login {}
