import Vue from 'vue'
import Router from 'vue-router'

import Main from '@/views/Main.vue'
import Auth from '@/views/Auth.vue'
import Board from "@/views/Board.vue";
import Servers from "@/views/Servers.vue";
import Account from "@/views/Account.vue";

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
    },

    {
      path: '/account',
      name: 'account',
      component: Account
    },

    {
      path: '/board',
      component: Main
    },

    {
      path: '/board/:id',
      name: 'board',
      component: Board
    }
  ]
})
