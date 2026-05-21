import { Component } from '@angular/core';
import { LucideAngularModule, Mail, Lock, ArrowRight, Eye } from 'lucide-angular';

@Component({
  selector: '[app-login-form]',
  imports: [LucideAngularModule],
  templateUrl: './login-form.html'
})
export class LoginForm {
  readonly Mail = Mail
  readonly Lock = Lock
  readonly ArrowRight = ArrowRight
  readonly Eye = Eye
}
