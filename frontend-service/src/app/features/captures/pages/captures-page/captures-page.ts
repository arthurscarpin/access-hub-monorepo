import { Component, inject, signal } from '@angular/core';
import { SharedSidebar } from '../../../../shared/components/shared-sidebar/shared-sidebar';
import { SharedMenu } from '../../../../shared/components/shared-menu/shared-menu';
import { SharedHeader } from '../../../../shared/components/shared-header/shared-header';
import { SharedHeaderConfig } from '../../../../shared/components/shared-header/shared-header-config';
import { SharedMenuConfig } from '../../../../shared/components/shared-menu/shared-menu.config';
import { CapturesDropzone } from "../../components/captures-dropzone/captures-dropzone";

@Component({
  standalone: true,
  selector: 'app-captures-page',
  imports: [SharedSidebar, SharedMenu, SharedHeader, CapturesDropzone],
  templateUrl: './captures-page.html'
})
export class CapturesPage {
  public readonly modalStage = signal(false);

   public readonly headerConfig: SharedHeaderConfig = {
    category: 'Operation',
    title: 'Captures',
    description: 'Upload a ZIP file containing images to automatically recognize license plates',
  };

  public readonly menuConfig: SharedMenuConfig = {
    category: 'Operation',
    title: 'Captures',
  };

  public openModal(): void {
    this.modalStage.set(true);
  }

  public closeModal(): void {
    this.modalStage.set(false);
  }
}
