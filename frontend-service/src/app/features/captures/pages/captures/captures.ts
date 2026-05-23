import { Component } from '@angular/core';
import { Sidebar } from '@components/shared/sidebar/sidebar';
import { TopMenu } from '../../../../shared/components/top-menu/top-menu';
import { PageTitle } from '../../../../shared/components/page-title/page-title';
import { CapturesDragDrop } from '../../components/captures-drag-drop/captures-drag-drop';
import { CapturesTable } from '../../components/captures-table/captures-table';

@Component({
  selector: 'app-captures',
  standalone: true,
  imports: [Sidebar, TopMenu, PageTitle, CapturesDragDrop, CapturesTable],
  templateUrl: './captures.html',
})
export class Captures {}