import { Component } from '@angular/core';
import { LucideAngularModule, FileText, Mail } from 'lucide-angular';

@Component({
  selector: 'app-owners-card',
  imports: [LucideAngularModule],
  templateUrl: './owners-card.html'
})
export class OwnersCard {
  readonly FileText: any = FileText;
  readonly Mail = Mail;
}
