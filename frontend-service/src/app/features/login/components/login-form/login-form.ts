import { Component } from '@angular/core';
import { LucideAngularModule, Mail, Lock, ArrowRight, Eye } from 'lucide-angular';

@Component({
  selector: '[app-login-form]',
  imports: [LucideAngularModule],
  templateUrl: './login-form.html'
})
export class LoginForm {
  readonly Mail: any = Mail
  readonly Lock: any = Lock
  readonly ArrowRight: any = ArrowRight
  readonly Eye: any = Eye
}
