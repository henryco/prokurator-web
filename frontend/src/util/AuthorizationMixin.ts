import Vue from 'vue';

const AUTH_PROP_NAME = 'Authorization'

declare interface AuthorizationMixin {
  authorize(token: string): void;
  removeAuthorization(): void;
  getAuthorization(): string | null;
  isAuthorized(): boolean;
}

declare module 'vue/types/vue' {
  export interface Vue extends AuthorizationMixin {}
}

export default Vue.mixin(Vue.extend({
  name: 'AuthorizationStoreMixin',
  methods: <AuthorizationMixin> {
    authorize: function (token: string): void {
      if (token === null || token === undefined)
        throw "There is no authorization token!"
      localStorage.setItem(AUTH_PROP_NAME, token)
    },

    removeAuthorization: function (): void {
      localStorage.removeItem(AUTH_PROP_NAME)
    },

    getAuthorization: function (): string | null {
      return localStorage.getItem(AUTH_PROP_NAME)
    },

    isAuthorized: function (): boolean {
      const a = localStorage.getItem(AUTH_PROP_NAME)
      return a !== null && a !== undefined
    }
  }
}))
