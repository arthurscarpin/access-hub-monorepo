import { Component, ElementRef, ViewChild, inject, signal } from '@angular/core';
import { LucideCloudUpload } from '@lucide/angular';
import { CaptureService } from '../../../../core/services/capture.service';
import { WebsocketService } from '../../../../core/services/websocket.service';

@Component({
  standalone: true,
  selector: 'app-captures-dropzone',
  imports: [LucideCloudUpload],
  templateUrl: './captures-dropzone.html',
})
export class CapturesDropzone {
  private readonly captureService = inject(CaptureService);
  private readonly wsService = inject(WebsocketService);

  @ViewChild('fileInput') fileInput!: ElementRef<HTMLInputElement>;

  protected readonly selectedFile = signal<File | null>(null);
  protected readonly message = signal<string | null>(null);
  protected readonly messageType = signal<'success' | 'error' | null>(null);
  protected readonly loading = signal(false);
  protected readonly direction = signal<'IN' | 'OUT'>('IN');

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

    this.captureService.upload(file, this.direction()).subscribe({
      next: (response: any) => {
        this.loading.set(false);

        this.message.set(response?.message ?? 'Upload completed successfully');
        this.messageType.set('success');

        console.log('Response received from upload:', response);

        if (response && response.id) {
          console.log(`Triggering channel for ID: ${response.id}`);
          this.wsService.initializeCaptureSubscription(response.id);
        } else if (response && response.data && response.data.id) {
          console.log(`Triggering channel for ID (via .data): ${response.data.id}`);
          this.wsService.initializeCaptureSubscription(response.data.id);
        } else if (response && response.payload && response.payload.id) {
          console.log(`Triggering channel for ID (via .payload): ${response.payload.id}`);
          this.wsService.initializeCaptureSubscription(response.payload.id);
        } else {
          console.warn('Upload succeeded, but capture ID was not found in the HTTP response.');
        }

        this.reset();
      },
      error: (error) => {
        this.loading.set(false);

        this.message.set(error?.error?.message ?? 'Upload failed');
        this.messageType.set('error');

        this.reset();
      },
    });
  }

  private reset(): void {
    this.selectedFile.set(null);

    if (this.fileInput) {
      this.fileInput.nativeElement.value = '';
    }
  }
}