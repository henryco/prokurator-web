import PrkGeneralApi, {Mock as MockGeneralApi, GeneralApi} from "./general/PrkGeneralApi";
import Index, {Mock as MockMediaApi, MediaApi} from "./media"

import AuthRequestMixin from "@/api/util/AuthRequestMixin";
import Vue from 'vue';

declare interface Api {
  general: PrkGeneralApi;
  media: Index;
}

declare interface ApiMixin {
  api(): Api;
}

declare module 'vue/types/vue' {
  export interface Vue extends ApiMixin {}
}

export default Vue.mixin(Vue.extend({
  name: 'ApiMixin',
  mixins: [AuthRequestMixin],

  methods: <ApiMixin> {
    api: () => (<Api> {
      general: new GeneralApi(),
      media: new MockMediaApi()
    })
  }
}));
