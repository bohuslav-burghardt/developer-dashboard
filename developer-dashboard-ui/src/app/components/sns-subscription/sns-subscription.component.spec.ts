import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SnsSubscriptionComponent } from './sns-subscription.component';

describe('SnsSubscriptionComponent', () => {
  let component: SnsSubscriptionComponent;
  let fixture: ComponentFixture<SnsSubscriptionComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ SnsSubscriptionComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(SnsSubscriptionComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
