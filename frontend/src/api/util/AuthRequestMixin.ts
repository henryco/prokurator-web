import authorization from "./AuthorizationMixin";
import axios from 'axios'
import Vue from 'vue';

export default Vue.mixin({
  name: 'AuthRequestMixin',
  mixins: [authorization],
  beforeCreate(): void {
    axios.interceptors.request.use(
      (config) => {
        config.headers['X-Requested-With'] = 'XMLHttpRequest'

        if (!config.headers.Authorization) {
          const token = this.getAuthorization()
          if (token) config.headers.Authorization = `${token}`
        }
        return config
      },
      (error) => Promise.reject(error)
    )
  }
})
