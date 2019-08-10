<template>
  <prk-top-bar class="top-bar">

    <div class="fonted" slot="left">
      <span class="title">Prokurator</span>
    </div>

    <template slot="right">
      <login-button v-if="!isAuthorized()"/>
      <el-dropdown class="menu" trigger="click" @command="_onAccountMenuClick" v-else>

        <div class="account-box">
          <el-avatar fit="cover" :src="c_accountIcon"></el-avatar>
          <span class="fonted acc-name">{{c_accountName}}</span>
        </div>

        <el-dropdown-menu slot="dropdown">
          <el-dropdown-item icon="el-icon-user" command="account">My Account</el-dropdown-item>
          <el-dropdown-item icon="el-icon-connection" command="servers">Servers</el-dropdown-item>
          <el-dropdown-item icon="el-icon-switch-button" command="logout">Logout</el-dropdown-item>
        </el-dropdown-menu>
      </el-dropdown>
    </template>

  </prk-top-bar>
</template>

<script lang="ts">
  import LoginButton from '@/composites/LoginButton.vue'
  import PrkTopBar from "@/components/PrkTopBar.vue";
  import Vue from 'vue';

  export default Vue.extend({
    name: "TopBar",
    components: {
      LoginButton,
      PrkTopBar
    },

    computed: {
      c_accountName: function () {
        // TODO
        return "Account"
      },
      c_accountIcon: function () {
        // TODO
        return "https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png"
      }
    },

    methods: {
      _onAccountMenuClick: function (option: string) {
        switch (option) {
          case 'account':this._account(); break;
          case 'servers':this._servers(); break;
          case 'logout':this._logout(); break;
        }
      },

      _account: function(): void {
        alert('account')
        // TODO
      },

      _servers: function(): void {
        this.$router.push({
          name: 'servers'
        })
      },

      _logout: function (): void {
        this.removeAuthorization()
        window.location.href = "/"
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

    .title {
      font-size: 24px;
      margin-left: 100px;
      font-weight: bold;
    }

    .menu {
      margin-right: 30px;
    }

    .account-box {
      cursor: pointer;
      display: flex;
      flex-direction: row;
      align-items: center;
      align-content: center;

      .acc-name {
        padding-left: 8px;
        padding-right: 8px;
        font-size: 14px;
      }
    }
  }

</style>
