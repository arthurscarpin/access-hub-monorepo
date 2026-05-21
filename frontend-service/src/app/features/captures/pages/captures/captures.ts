import { Component } from '@angular/core';
import { SideMenu } from '../../../../shared/components/side-menu/side-menu';
import { TopMenu } from '../../../../shared/components/top-menu/top-menu';
import { PageTitle } from '../../../../shared/components/page-title/page-title';
import { CapturesDragDrop } from '../../components/captures-drag-drop/captures-drag-drop';
import { CapturesTable } from '../../components/captures-table/captures-table';

import { LucideAngularModule, CloudUpload } from 'lucide-angular';

@Component({
  selector: 'app-captures',
  standalone: true,
  imports: [LucideAngularModule, SideMenu, TopMenu, PageTitle, CapturesDragDrop, CapturesTable],
  templateUrl: './captures.html',
})
export class Captures {
  readonly CloudUpload: any = CloudUpload;
}