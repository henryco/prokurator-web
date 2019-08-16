
declare interface Activator {
  absolute?: number | string;
  pixels?: number | string;
}

class ActivatorProps implements Activator {
  absolute?: number | string;
  pixels?: number | string;
}
export {ActivatorProps, Activator};


declare interface LoadEvent {
  absolute?: number;
  pixels?: number;
  next: Function;
}
export {LoadEvent}

const INFINITY: string = 'infinity'
const EVENT: string = 'fetch';
export {EVENT, INFINITY}

import PrkInfinityScroll from "./PrkInfinityScroll.vue";
export default PrkInfinityScroll;
