import { SaneDatePipe } from './sane-date.pipe';

describe('SaneDatePipe', () => {
  it('create an instance', () => {
    const pipe = new SaneDatePipe();
    expect(pipe).toBeTruthy();
  });
});
