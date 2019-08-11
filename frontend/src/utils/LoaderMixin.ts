import {ElLoadingComponent, LoadingServiceOptions} from "element-ui/types/loading";
import {Loading} from 'element-ui';
import Vue from "vue";

declare interface LoaderMixin {
  loadingService(options: LoadingServiceOptions): ElLoadingComponent;
}

declare module 'vue/types/vue' {
  export interface Vue extends LoaderMixin {}
}

export default Vue.mixin(Vue.extend({
  name: 'LoaderMixin',
  methods: <LoaderMixin> {
    loadingService(options: LoadingServiceOptions): ElLoadingComponent {
      return Loading.service(options);
    }
  }
}));
