import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'saneDateTime'
})
export class SaneDateTimePipe implements PipeTransform {

  transform(value: any, ...args: any[]): any {
    if (!value) {
      return value;
    }
    const d = new Date(typeof value === "number" ? value : Date.parse(value));
    return `${this.lp(d.getDate())}.${this.lp(d.getMonth() + 1)}.${this.lp(d.getFullYear())} - ${this.lp(d.getHours())}:${this.lp(d.getMinutes())}:${this.lp(d.getSeconds())}`;
  }

  lp(input: number) {
    if (input < 10) {
      return '0' + input;
    }
    return input;
  }

}
