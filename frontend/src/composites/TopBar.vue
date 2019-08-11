<template>
  <prk-top-bar class="top-bar">

    <div class="fonted left-box" slot="left" @click="_home">
      <span class="title">Prokurator</span>
    </div>

    <template slot="right">
      <login-button v-if="!isAuthorized()"/>
      <el-dropdown class="menu" trigger="click" @command="_onAccountMenuClick" v-else>

        <div class="account-box">
          <el-avatar fit="cover" :src="usericon"></el-avatar>
          <span class="fonted acc-name">{{username}}</span>
        </div>

        <el-dropdown-menu slot="dropdown">
          <el-dropdown-item icon="el-icon-user" command="account">{{strings.account}}</el-dropdown-item>
          <el-dropdown-item icon="el-icon-connection" command="servers">{{strings.servers}}</el-dropdown-item>
          <el-dropdown-item icon="el-icon-switch-button" command="logout">{{strings.logout}}</el-dropdown-item>
        </el-dropdown-menu>
      </el-dropdown>
    </template>

  </prk-top-bar>
</template>

<script lang="ts">
  import LoginButton from '@/composites/LoginButton.vue'
  import PrkTopBar from "@/components/PrkTopBar.vue";
  import Vue from 'vue';

  interface Data {
    username?: string;
    usericon?: string;
  }

  export default Vue.extend({
    name: "TopBar",
    components: {
      LoginButton,
      PrkTopBar
    },

   data: () =>(<Data> {
     usericon: undefined,
     username: ''
   }),

    methods: {
      _onAccountMenuClick: function (option: string) {
        switch (option) {
          case 'account':this._account(); break;
          case 'servers':this._servers(); break;
          case 'logout':this._logout(); break;
        }
      },

      _home: function(): void {
        this.$router.push({
          name: 'main'
        })
      },

      _account: function(): void {
        this.$router.push({
          name: 'account'
        })
      },

      _servers: function(): void {
        this.$router.push({
          name: 'servers'
        })
      },

      _logout: function (): void {
        this.removeAuthorization()
        window.location.href = "/"
      },

      _fetchUserData: async function () {
        if (!this.isAuthorized()) return;

        const user = await this.api().general.getUserInfo();
        this.username = user.name
        this.usericon = user.icon
      }
    },

    mounted(): void {
      this.$nextTick(async () => this._fetchUserData())
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
