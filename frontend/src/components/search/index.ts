declare interface Context {
  value: string;
  name: string;
}

declare interface Filter {
  value: string | number;
  type: Context;
}

declare interface Query {
  filters: Context[]
  raw: string;
}

declare type Fetch = (context: string, query: string) => Promise<Context[]>;

export {Context, Filter, Fetch, Query};

import PrkSearchInput from "@/components/search/PrkSearchInput.vue";
export default PrkSearchInput;

