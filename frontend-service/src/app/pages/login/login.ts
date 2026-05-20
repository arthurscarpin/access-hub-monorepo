import { Component } from '@angular/core';
import { AuthLayout } from '../../components/auth-layout/auth-layout';
import { LoginForm } from '../../components/login-form/login-form';

@Component({
  selector: 'app-login',
  imports: [AuthLayout, LoginForm],
  templateUrl: './login.html',
  styleUrl: './login.css',
})
export class Login {}
