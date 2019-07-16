import Vue from 'vue'
import './plugins/vuetify'
import App from './App.vue'
import router from './router/router'
import store from './store/store'

import VueCookies from "vue-cookies-ts"
import Cookies from "vue-cookies"
Vue.use(VueCookies)
Vue.use(Cookies)

Vue.config.productionTip = false;

new Vue({
  router,
  store,
  render: h => h(App)
}).$mount('#app');
