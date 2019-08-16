declare interface I8NStrings {
  selectServer: string;
  account: string;
  servers: string;
  logout: string;
  prokurator: string;
}

declare interface I8NMixin {
  strings: I8NStrings;
  getStrings(): I8NStrings;
}

declare module 'vue/types/vue' {
  export interface Vue extends I8NMixin {}
}

const I8NEng: I8NStrings = {
  selectServer: 'Select a server',
  account: "My Account",
  logout: "Logout",
  servers: "Servers",
  prokurator: "Prokurator"
}

import Vue from "vue";
export default Vue.mixin(Vue.extend({
  name: 'i8nMixin',
  computed: {
    strings: () => I8NEng
  },
  methods: <I8NMixin> {
    getStrings: () => I8NEng
  }
}));
