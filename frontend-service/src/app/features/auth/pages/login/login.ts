import { Component } from '@angular/core';
import {
  LucideAngularModule,
  ShieldCheck,
  Mail,
  Lock,
  Eye,
  ArrowRight
} from 'lucide-angular';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [LucideAngularModule],
  templateUrl: './login.html',
})
export class Login {
  readonly ShieldCheck = ShieldCheck;
  readonly Mail = Mail;
  readonly Lock = Lock;
  readonly Eye = Eye;
  readonly ArrowRight = ArrowRight;
}