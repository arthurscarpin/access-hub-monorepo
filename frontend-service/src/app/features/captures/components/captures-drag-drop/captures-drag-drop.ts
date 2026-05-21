import { Component } from '@angular/core';
import { LucideAngularModule, CloudUpload } from 'lucide-angular';

@Component({
  selector: 'app-captures-drag-drop',
  imports: [LucideAngularModule],
  templateUrl: './captures-drag-drop.html'
})
export class CapturesDragDrop {
  readonly CloudUpload = CloudUpload;
}
