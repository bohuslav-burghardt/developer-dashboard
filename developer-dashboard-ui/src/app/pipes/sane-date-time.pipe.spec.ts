import { SaneDateTimePipe } from './sane-date-time.pipe';

describe('SaneDateTimePipe', () => {
  it('create an instance', () => {
    const pipe = new SaneDateTimePipe();
    expect(pipe).toBeTruthy();
  });
});
