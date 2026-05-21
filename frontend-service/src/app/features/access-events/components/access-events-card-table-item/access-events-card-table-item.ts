import { Component } from '@angular/core';
import { LucideAngularModule, ArrowDownLeft} from 'lucide-angular';

@Component({
  selector: '[app-access-events-card-table-item]',
  imports: [LucideAngularModule],
  templateUrl: './access-events-card-table-item.html',
})
export class AccessEventsCardTableItem {
  readonly ArrowDownLeft = ArrowDownLeft;
}
