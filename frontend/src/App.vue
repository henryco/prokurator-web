<template>
  <v-app>
    <top-bar/>
    <v-layout row pb-2>
      <v-flex xs8 offset-xs2>
        <router-view/>
      </v-flex>
    </v-layout>
  </v-app>
</template>

<script lang="ts">
  import Vue from "vue"

  import TopBar from "@/components/TopBar.vue"
  import axios from 'axios'

  export default Vue.extend({
    name: 'App',
    components: {
      TopBar
    },

    beforeCreate(): void {
      axios.interceptors.request.use(
        (config) => {
          config.headers["X-Requested-With"] = "XMLHttpRequest"
          if (!config.headers.Authorization) {

            // @ts-ignore
            const token = window.$cookies.get('Authorization')
            if (token) config.headers.Authorization = `${token}`
          }
          return config
        },
        error => Promise.reject(error)
      )
    }

  })
</script>

<style lang="scss">
  html {
    overflow-y: auto !important;
  }
</style>
