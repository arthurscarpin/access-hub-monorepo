import { Component } from '@angular/core';
import { CommonModule, NgClass, NgComponentOutlet } from '@angular/common';

import { Sidebar } from 'src/app/ui/components/shared/sidebar/sidebar';
import { Menu } from 'src/app/ui/components/shared/menu/menu';
import { PageHeader } from 'src/app/ui/components/shared/page-header/page-header';
import { Button } from 'src/app/ui/components/shared/button/button';

import { PROCESSING_HISTORY_OPTIONS } from '@ui/pages/captures/captures.types';
import { LucideCloudUpload } from '@lucide/angular';

@Component({
  standalone: true,
  selector: 'app-captures',
  imports: [
    CommonModule,
    NgClass,
    NgComponentOutlet,
    Sidebar,
    Menu,
    PageHeader,
    Button,
    LucideCloudUpload,
  ],
  templateUrl: './captures.html',
})
export class Captures {
  isDragging = false;
  selectedFile: File | null = null;

  breadCrumbOperation = 'Operation';
  breadCrumbName = 'Captures';

  pageTitle = 'Captures';
  pageCategory = 'Operation';
  pageDescription =
    'Send and process batches of images in ZIP format for automatic license plate recognition.';

  buttonLabel = 'Send Capture';

  buttonStyle =
    'rounded-xl bg-slate-900 px-5 py-3 text-sm font-medium text-white transition hover:bg-slate-800 cursor-pointer';

  processingHistory = PROCESSING_HISTORY_OPTIONS;

  onDragEnter(event: DragEvent) {
    event.preventDefault();
    event.stopPropagation();
    this.isDragging = true;
  }

  onDragOver(event: DragEvent) {
    event.preventDefault();
    event.stopPropagation();
    this.isDragging = true;
  }

  onDragLeave(event: DragEvent) {
    event.preventDefault();
    event.stopPropagation();
    this.isDragging = false;
  }

  onDrop(event: DragEvent) {
    event.preventDefault();
    event.stopPropagation();

    this.isDragging = false;

    const file = event.dataTransfer?.files?.[0];

    if (file) {
      this.handleFile(file);
    }
  }

  onFileSelect(event: Event) {
    const input = event.target as HTMLInputElement;

    const file = input.files?.[0];

    if (file) {
      this.handleFile(file);
    }

    input.value = '';
  }

  private handleFile(file: File) {
    if (!file.name.toLowerCase().endsWith('.zip')) return;

    this.selectedFile = file;

    // this.uploadService.uploadZip(this.selectedFile)
  }

  clearFile() {
    this.selectedFile = null;
  }
}
