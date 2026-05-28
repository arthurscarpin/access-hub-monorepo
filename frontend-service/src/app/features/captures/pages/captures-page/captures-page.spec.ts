import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CapturesPage } from './captures-page';

describe('CapturesPage', () => {
  let component: CapturesPage;
  let fixture: ComponentFixture<CapturesPage>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [CapturesPage],
    }).compileComponents();

    fixture = TestBed.createComponent(CapturesPage);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
