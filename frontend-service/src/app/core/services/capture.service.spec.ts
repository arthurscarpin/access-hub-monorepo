import { provideHttpClient } from '@angular/common/http';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';

import { environment } from '../../../environments/environment';
import { CaptureService } from './capture.service';

describe('CaptureService', () => {
  let service: CaptureService;
  let http: HttpTestingController;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });

    service = TestBed.inject(CaptureService);
    http = TestBed.inject(HttpTestingController);
  });

  afterEach(() => http.verify());

  it('uploads a ZIP file with the selected direction', () => {
    const file = new File(['zip-content'], 'captures.zip', { type: 'application/zip' });

    service.upload(file, 'OUT').subscribe((capture) => expect(capture.id).toBe('capture-1'));

    const req = http.expectOne(`${environment.apiUrl}/captures/upload`);
    expect(req.request.method).toBe('POST');
    expect(req.request.body instanceof FormData).toBe(true);
    expect(req.request.body.get('file')).toBe(file);
    expect(req.request.body.get('direction')).toBe('OUT');
    req.flush({
      id: 'capture-1',
      captureStatus: 'RECEIVED',
      message: 'ok',
      extractedFiles: [],
    });
  });
});
