import { Component } from '@angular/core';
import { LucideAngularModule, FileArchive, ArrowDownLeft } from 'lucide-angular';

@Component({
  selector: 'app-captures-table-item',
  imports: [LucideAngularModule],
  templateUrl: './captures-table-item.html'
})
export class CapturesTableItem {
  readonly FileArchive: any = FileArchive;
  readonly ArrowDownLeft: any = ArrowDownLeft;
}
