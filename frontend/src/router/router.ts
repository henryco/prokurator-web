import Vue from 'vue'
import Router from 'vue-router'
import Main from '@/views/Main.vue'
import Login from '@/views/Login.vue'

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
      component: Login
    }
  ]
})
