import { ComponentFixture, TestBed } from '@angular/core/testing';
import { of, throwError } from 'rxjs';

import { CaptureService } from '../../../../core/services/capture.service';
import { WebsocketService } from '../../../../core/services/websocket.service';
import { CapturesDropzone } from './captures-dropzone';

describe('CapturesDropzone', () => {
  let component: CapturesDropzone;
  let fixture: ComponentFixture<CapturesDropzone>;
  let captureService: { upload: ReturnType<typeof vi.fn> };
  let websocketService: { initializeCaptureSubscription: ReturnType<typeof vi.fn> };

  beforeEach(async () => {
    captureService = { upload: vi.fn() };
    websocketService = { initializeCaptureSubscription: vi.fn() };

    await TestBed.configureTestingModule({
      imports: [CapturesDropzone],
      providers: [
        { provide: CaptureService, useValue: captureService },
        { provide: WebsocketService, useValue: websocketService },
      ],
    }).compileComponents();

    fixture = TestBed.createComponent(CapturesDropzone);
    component = fixture.componentInstance;
    fixture.detectChanges();
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should reject files that are not ZIP archives', () => {
    const file = new File(['content'], 'capture.txt');

    (component as any).onFileSelected({
      target: { files: [file] },
    } as unknown as Event);
    fixture.detectChanges();

    expect((fixture.nativeElement as HTMLElement).textContent).toContain('Only .zip files are allowed');
    expect(captureService.upload).not.toHaveBeenCalled();
  });

  it('should upload the selected ZIP and start websocket monitoring', () => {
    const file = new File(['content'], 'capture.zip');
    captureService.upload.mockReturnValue(of({ id: 'capture-1', message: 'Upload complete' }));

    (component as any).onFileSelected({
      target: { files: [file] },
    } as unknown as Event);
    (component as any).direction.set('OUT');
    (component as any).upload();

    expect(captureService.upload).toHaveBeenCalledWith(file, 'OUT');
    expect(websocketService.initializeCaptureSubscription).toHaveBeenCalledWith('capture-1');
    expect((component as any).selectedFile()).toBeNull();
  });

  it('should show a fallback upload error message', () => {
    const file = new File(['content'], 'capture.zip');
    captureService.upload.mockReturnValue(throwError(() => ({ error: {} })));

    (component as any).onFileSelected({
      target: { files: [file] },
    } as unknown as Event);
    (component as any).upload();

    expect((component as any).message()).toBe('Upload failed');
    expect((component as any).messageType()).toBe('error');
  });
});
