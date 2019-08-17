declare interface Context {
  value: string;
  name: string;
}

declare interface Filter {
  value: any;
  type: Context;
  name?: string;
}

declare interface Query {
  filters: Context[]
  raw: string;
}

export const DATE: Context = {
  value: '<$%=date=%$>',
  name: '<$%=date=%$>'
}

declare type Fetch = (context: string, query: string) => Promise<Context[]>;

export {Context, Filter, Fetch, Query};

import PrkSearchInput from "@/components/search/PrkSearchInput.vue";
export default PrkSearchInput;

