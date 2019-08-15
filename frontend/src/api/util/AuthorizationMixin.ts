import Vue from 'vue';

const AUTH_PROP_NAME = 'Authorization'

declare interface AuthorizationMixinData {
  authorization?: string;
}

declare interface AuthorizationMixin extends AuthorizationMixinData{
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

  data: function () {
    return <AuthorizationMixinData> {
      get authorization (): string | undefined {
        return localStorage.getItem(AUTH_PROP_NAME) || undefined
      },
      set authorization (value: string | undefined) {
        if (value === undefined)
          localStorage.removeItem(AUTH_PROP_NAME)
        else localStorage.setItem(AUTH_PROP_NAME, value)
      }
    }
  },

  methods: <AuthorizationMixin> {
    authorize: function (token: string): void {
      if (token === null || token === undefined)
        throw "There is no authorization token!"
      this.authorization = token;
    },

    removeAuthorization: function (): void {
      this.authorization = undefined;
    },

    getAuthorization: function (): string | null {
      return this.authorization || null
    },

    isAuthorized: function (): boolean {
      return this.authorization !== undefined;
    }
  },

  watch: {
    authorization: function (v) {
      console.log('mixin update: ', v)
    }
  }
}))
