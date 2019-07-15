<template>
  <v-app>
    <router-view/>
  </v-app>
</template>

<script>
  import axios from 'axios'

  export default {
    name: 'App',

    mounted () {

      axios.interceptors.request.use(
        (config) => {
          config.headers["X-Requested-With"] = "XMLHttpRequest"
          if (!config.headers.Authorization) {
            const token = this.$cookies.get('Authorization')
            if (token) config.headers.Authorization = `${token}`
          }
          return config
        },
        error => Promise.reject(error)
      )
    }
  }
</script>
