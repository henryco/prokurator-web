import Vue from 'vue'
import Router from 'vue-router'

import Main from '@/views/Main.vue'
import Auth from '@/views/Auth.vue'
import Servers from "@/views/Servers.vue";

Vue.use(Router)

export default new Router({
  routes: [
    {
      path: '/',
      name: 'main',
      component: Main
    },
    {
      path: '/auth',
      name: 'auth',
      component: Auth
    },
    {
      path: '/servers',
      name: 'servers',
      component: Servers
    }
  ]
})
