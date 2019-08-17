<template>
  <prk-top-bar class="top-bar">

    <div class="fonted left-box" slot="left" @click="_home">
      <span class="title">{{strings.prokurator}}</span>
    </div>

    <template slot="right">
      <account-button
        @account="_account"
        @servers="_servers"
        @logout="_logout"
        v-if="authorized"
      />
      <login-button
        @login="_login"
        v-else
      />
    </template>

  </prk-top-bar>
</template>

<script lang="ts">
  import AccountButton from "@/composites/top/AccountButton.vue";
  import LoginButton from '@/composites/top/LoginButton.vue'
  import PrkTopBar from "@/components/top/PrkTopBar.vue";
  import Vue from 'vue';

  export default Vue.extend({
    name: "TopBar",

    components: {
      AccountButton,
      LoginButton,
      PrkTopBar
    },

    props: {
      authorized: [Boolean]
    },

    methods: {
      _home: function(): void {
        this.$router.push({
          name: 'main'
        })
      },

      _account: function(): void {
        this.$router.push({name: 'account'})
      },

      _servers: function(): void {
        this.$router.push({name: 'servers'})
      },

      _login: function (): void {
        localStorage.setItem('router_cache', window.location.pathname)
        window.location.assign("/login")
      },

      _logout: function (): void {
        this.removeAuthorization()
        this.$nextTick(() => {
          window.location.assign("/");
        })
      }
    }
  });
</script>

<style scoped lang="scss">
  .top-bar {
    position: relative;
    width: 100%;
    height: 80px;

    -webkit-touch-callout: none; /* iOS Safari */
    -webkit-user-select: none; /* Safari */
    -moz-user-select: none; /* Firefox */
    -ms-user-select: none; /* Internet Explorer/Edge */
    user-select: none;

    .fonted {
      font-family: "Helvetica Neue", Helvetica, "DejaVu Sans Light", Arial, sans-serif;
      color: #606266;
    }

    .left-box {
      cursor: pointer;
    }

    .title {
      font-size: 24px;
      margin-left: 100px;
      font-weight: bold;
    }
  }
</style>
