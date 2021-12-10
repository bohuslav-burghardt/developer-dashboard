import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ListenToQueueComponent } from './listen-to-queue.component';

describe('ListenToQueueComponent', () => {
  let component: ListenToQueueComponent;
  let fixture: ComponentFixture<ListenToQueueComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ListenToQueueComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ListenToQueueComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
