import { Component } from '@angular/core';
import { CapturesTableItem } from '../captures-table-item/captures-table-item';

@Component({
  selector: 'app-captures-table',
  imports: [CapturesTableItem],
  templateUrl: './captures-table.html'
})
export class CapturesTable {}
