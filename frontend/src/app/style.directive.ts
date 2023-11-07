import { Directive, HostBinding } from '@angular/core';

@Directive({
  selector: '[click]'
})
export class StyleDirective {

  constructor() { }
  @HostBinding('style.cursor') cursor: string = 'pointer';

}
