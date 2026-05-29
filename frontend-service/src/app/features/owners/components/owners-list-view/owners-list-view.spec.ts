import { signal } from '@angular/core';
import { ComponentFixture, TestBed } from '@angular/core/testing';

import { OwnerService } from '../../../../core/services/owner.service';
import { paginatedFixture } from '../../../../testing/test-fixtures';
import { OwnersListView } from './owners-list-view';

describe('OwnersListView', () => {
  let component: OwnersListView;
  let fixture: ComponentFixture<OwnersListView>;
  let service: {
    owners: ReturnType<typeof signal>;
    loading: ReturnType<typeof signal>;
    totalElements: ReturnType<typeof signal>;
    pagination: ReturnType<typeof signal>;
    findAll: ReturnType<typeof vi.fn>;
  };

  beforeEach(async () => {
    service = {
      owners: signal([]),
      loading: signal(false),
      totalElements: signal(0),
      pagination: signal(null),
      findAll: vi.fn(),
    };

    await TestBed.configureTestingModule({
      imports: [OwnersListView],
      providers: [{ provide: OwnerService, useValue: service }],
    }).compileComponents();

    fixture = TestBed.createComponent(OwnersListView);
    component = fixture.componentInstance;
    fixture.detectChanges();
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should load owners on init and paginate inside bounds', () => {
    expect(service.findAll).toHaveBeenCalledWith();

    service.pagination.set(paginatedFixture([], { number: 1, first: false, last: false }));
    component.changePage('next');
    component.changePage('previous');

    expect(service.findAll).toHaveBeenCalledWith(2);
    expect(service.findAll).toHaveBeenCalledWith(0);
  });

  it('should format CPF and RG documents', () => {
    expect(component.formatDocument('12345678901', 'CPF')).toBe('123.456.789-01');
    expect(component.formatDocument('123456789', 'RG')).toBe('12.345.678-9');
    expect(component.formatDocument('A1B2', 'OTHER')).toBe('12');
  });
});
