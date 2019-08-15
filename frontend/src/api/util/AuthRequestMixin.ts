import authorization from "./AuthorizationMixin";
import axios, {AxiosError} from 'axios'
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

    const errorHandler = (error: AxiosError) => {
      const response = error.response
      if (error.code === '401' || (response && response.status === 401)) {
        this.removeAuthorization()
        window.location.href = "/"
      }
      return Promise.reject({ ...error })
    }

    axios.interceptors.response.use(
      v => v,
      error => errorHandler(error)
    )
  }
})
