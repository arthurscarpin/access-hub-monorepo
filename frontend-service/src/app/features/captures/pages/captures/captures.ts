import { Component } from '@angular/core';
import { Sidebar } from '@components/shared/sidebar/sidebar';
import { CapturesDragDrop } from '../../components/captures-drag-drop/captures-drag-drop';
import { CapturesTable } from '../../components/captures-table/captures-table';

@Component({
  selector: 'app-captures',
  standalone: true,
  imports: [Sidebar, CapturesDragDrop, CapturesTable],
  templateUrl: './captures.html',
})
export class Captures {}