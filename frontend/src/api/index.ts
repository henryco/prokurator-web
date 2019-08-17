import PrkGeneralApi, {Mock as MockGeneralApi, GeneralApi} from "./general/PrkGeneralApi";
import PrkMediaApi, {Mock as MockMediaApi, MediaApi} from "./media/PrkMediaApi"

import AuthRequestMixin from "@/api/util/AuthRequestMixin";
import Vue from 'vue';

declare interface Api {
  general: PrkGeneralApi;
  media: PrkMediaApi;
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
