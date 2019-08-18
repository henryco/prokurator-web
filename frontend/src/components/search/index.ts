declare interface Context {
  value: string;
  name: string;
  label?: string;
}

declare interface Filter {
  value: any;
  type: Context;
  name?: string;
}

declare interface Result
  extends Context {
}

declare interface Query {
  filters: Result[]
  raw: string;
}

export const DATE: Context = {
  value: '<$%=date=%$>',
  name: '<$%=date=%$>'
}

declare type Fetch = (context: string, query: string) => Promise<Context[]>;

export {Context, Filter, Fetch, Query, Result};

import PrkSearchInput from "@/components/search/PrkSearchInput.vue";
export default PrkSearchInput;

