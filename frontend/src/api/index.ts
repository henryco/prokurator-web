import PrkGeneralApi, {Mock, GeneralApi} from "@/api/general/PrkGeneralApi";
import AuthRequestMixin from "@/api/util/AuthRequestMixin";
import Vue from 'vue';

declare interface Api {
  general: PrkGeneralApi;
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

      // TODO MORE API
      general: new Mock(),

    })
  }
}));
