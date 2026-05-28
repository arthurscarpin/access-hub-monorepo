import { Component, inject, signal } from '@angular/core';
import { LucideCloudUpload } from '@lucide/angular';

import { CaptureService } from '../../../../core/services/capture.service';

@Component({
  standalone: true,
  selector: 'app-captures-dropzone',
  imports: [LucideCloudUpload],
  templateUrl: './captures-dropzone.html',
})
export class CapturesDropzone {
  private readonly captureService = inject(CaptureService);

  protected readonly selectedFile = signal<File | null>(null);

  protected readonly message = signal<string | null>(null);
  protected readonly messageType = signal<'success' | 'error' | null>(null);
  protected readonly loading = signal(false);

  protected onFileSelected(event: Event): void {
    const input = event.target as HTMLInputElement;

    if (!input.files?.length) return;

    const file = input.files[0];

    if (!file.name.endsWith('.zip')) {
      this.message.set('Only .zip files are allowed');
      this.messageType.set('error');
      return;
    }

    this.selectedFile.set(file);
    this.message.set(null);
    this.messageType.set(null);
  }

  protected upload(): void {
    const file = this.selectedFile();

    if (!file) return;

    this.loading.set(true);
    this.message.set(null);
    this.messageType.set(null);

    this.captureService.upload(file, 'IN').subscribe({
      next: (response) => {
        this.loading.set(false);

        this.message.set(response.message ?? 'Upload completed successfully');
        this.messageType.set('success');

        this.selectedFile.set(null);
      },
      error: (error) => {
        this.loading.set(false);

        const backendMessage =
          error?.error?.message ?? 'Upload failed';

        this.message.set(backendMessage);
        this.messageType.set('error');
      },
    });
  }
}