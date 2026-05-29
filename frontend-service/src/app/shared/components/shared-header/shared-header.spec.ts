import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SharedHeader } from './shared-header';

describe('SharedHeader', () => {
  let component: SharedHeader;
  let fixture: ComponentFixture<SharedHeader>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [SharedHeader],
    }).compileComponents();

    fixture = TestBed.createComponent(SharedHeader);
    component = fixture.componentInstance;
    component.config = {
      category: 'Management',
      title: 'Owners',
      description: 'Control panel for owner management',
    };
    fixture.detectChanges();
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should render the configured category, title and description', () => {
    const text = (fixture.nativeElement as HTMLElement).textContent ?? '';

    expect(text).toContain('Management');
    expect(text).toContain('Owners');
    expect(text).toContain('Control panel for owner management');
  });
});
