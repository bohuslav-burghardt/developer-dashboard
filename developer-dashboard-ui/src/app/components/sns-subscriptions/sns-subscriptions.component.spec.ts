import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SnsSubscriptionsComponent } from './sns-subscriptions.component';

describe('SnsSubscriptionsComponent', () => {
  let component: SnsSubscriptionsComponent;
  let fixture: ComponentFixture<SnsSubscriptionsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ SnsSubscriptionsComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(SnsSubscriptionsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
