import { TestBed } from '@angular/core/testing';

import { SnsSubscriptionService } from './sns-subscription.service';

describe('SnsSubscriptionService', () => {
  let service: SnsSubscriptionService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(SnsSubscriptionService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
