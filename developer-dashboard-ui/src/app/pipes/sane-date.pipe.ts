import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'saneDate'
})
export class SaneDatePipe implements PipeTransform {

  transform(value: any, ...args: any[]): any {
    if (!value) {
      return value
    }
    const d = new Date(typeof value === "number" ? value : Date.parse(value));
    return `${this.lp(d.getDate())}.${this.lp(d.getMonth() + 1)}.${this.lp(d.getFullYear())}`;
  }

  lp(input: number) {
    if (input < 10) {
      return '0' + input
    }
    return input
  }

}
