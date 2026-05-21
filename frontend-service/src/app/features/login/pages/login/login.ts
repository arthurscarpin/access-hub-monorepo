import { Component } from '@angular/core';
import { LoginBanner } from '../../components/login-banner/login-banner';
import { LoginForm } from '../../components/login-form/login-form';


@Component({
  selector: 'app-login',
  standalone: true,
  imports: [LoginBanner, LoginForm],
  templateUrl: './login.html',
})
export class Login {}
