import { ComponentFixture, TestBed } from '@angular/core/testing';

import { RoCalendarComponent } from './ro-calendar.component';

describe('RoCalendarComponent', () => {
  let component: RoCalendarComponent;
  let fixture: ComponentFixture<RoCalendarComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ RoCalendarComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(RoCalendarComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
