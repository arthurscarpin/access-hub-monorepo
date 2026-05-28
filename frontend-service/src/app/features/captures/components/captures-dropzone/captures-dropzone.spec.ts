import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CapturesDropzone } from './captures-dropzone';

describe('CapturesDropzone', () => {
  let component: CapturesDropzone;
  let fixture: ComponentFixture<CapturesDropzone>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [CapturesDropzone],
    }).compileComponents();

    fixture = TestBed.createComponent(CapturesDropzone);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
