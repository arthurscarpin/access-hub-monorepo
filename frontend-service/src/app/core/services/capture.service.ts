import { Injectable, inject, signal } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

import { environment } from '../../../environments/environment';

import { Capture } from '../models/ capture.model';

@Injectable({ providedIn: 'root' })
export class CaptureService {
  private readonly apiUrl = environment.apiUrl;
  private readonly httpClient = inject(HttpClient);

  private readonly _captures = signal<Capture[]>([]);
  public readonly captures = this._captures.asReadonly();

  public upload(file: File, direction: string): Observable<Capture> {
    const formData = new FormData();

    formData.append('file', file);
    formData.append('direction', direction);

    return this.httpClient.post<Capture>(`${this.apiUrl}/captures/upload`, formData);
  }
}
