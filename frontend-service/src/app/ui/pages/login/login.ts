import { Component } from '@angular/core';
import { LoginSectionBanner } from 'src/app/ui/components/login/login-section-banner/login-section-banner';
import { LoginSectionForm } from 'src/app/ui/components/login/login-section-form/login-section-form';

@Component({
  standalone: true,
  selector: 'app-login',
  imports: [LoginSectionBanner, LoginSectionForm],
  templateUrl: './login.html',
})
export class Login {}
