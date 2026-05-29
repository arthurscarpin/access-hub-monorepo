import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter } from '@angular/router';

import { AuthService } from '../../../core/services/auth.service';
import { SharedSidebar } from './shared-sidebar';

describe('SharedSidebar', () => {
  let component: SharedSidebar;
  let fixture: ComponentFixture<SharedSidebar>;
  let authService: {
    getLoginInfo: ReturnType<typeof vi.fn>;
    logout: ReturnType<typeof vi.fn>;
  };

  beforeEach(async () => {
    authService = {
      getLoginInfo: vi.fn().mockReturnValue({
        email: 'admin@accesshub.test',
        username: 'admin',
      }),
      logout: vi.fn(),
    };

    await TestBed.configureTestingModule({
      imports: [SharedSidebar],
      providers: [provideRouter([]), { provide: AuthService, useValue: authService }],
    }).compileComponents();

    fixture = TestBed.createComponent(SharedSidebar);
    component = fixture.componentInstance;
    fixture.detectChanges();
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should render login info from auth service', () => {
    expect(component.email()).toBe('admin@accesshub.test');
    expect(component.username()).toBe('admin');

    const text = (fixture.nativeElement as HTMLElement).textContent ?? '';
    expect(text).toContain('admin@accesshub.test');
    expect(text).toContain('admin');
  });

  it('should delegate logout to auth service', () => {
    component.logout();

    expect(authService.logout).toHaveBeenCalled();
  });
});
