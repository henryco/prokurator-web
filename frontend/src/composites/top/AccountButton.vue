<template>
  <el-dropdown class="account-button" trigger="click" @command="_onAccountMenuClick" v-else>
    <div class="account-box">
      <el-avatar fit="cover" :src="d_usericon" v-if="d_usericon && d_usericon !== 'null'"></el-avatar>
      <el-avatar fit="cover" icon="el-icon-user" v-else></el-avatar>
      <span class="fonted acc-name">{{d_username}}</span>
    </div>
    <el-dropdown-menu slot="dropdown">
      <el-dropdown-item icon="el-icon-user" command="account">{{strings.account}}</el-dropdown-item>
      <el-dropdown-item icon="el-icon-connection" command="servers">{{strings.servers}}</el-dropdown-item>
      <el-dropdown-item icon="el-icon-switch-button" command="logout">{{strings.logout}}</el-dropdown-item>
    </el-dropdown-menu>
  </el-dropdown>
</template>

<script lang="ts">
  import Vue from 'vue';

  interface Data {
    d_username?: string | null;
    d_usericon?: string | null;
  }

  export default Vue.extend({
    name: "AccountButton",

    data: () =>(<Data> {
      d_usericon: undefined,
      d_username: ''
    }),

    methods: {
      _onAccountMenuClick: function (option: string) {
        switch (option) {
          case 'account': this.$emit('account'); break;
          case 'servers': this.$emit('servers'); break;
          case 'logout': this.$emit('logout'); break;
        }
      }
    },

    mounted(): void {
      this.$nextTick(async () => {
        const user = await this.api().general.getUserInfo();
        this.d_username = user.name
        this.d_usericon = user.icon
      })
    }

  });
</script>

<style scoped lang="scss">
  .account-button {
    margin-right: 30px;

    -webkit-touch-callout: none; /* iOS Safari */
    -webkit-user-select: none; /* Safari */
    -moz-user-select: none; /* Firefox */
    -ms-user-select: none; /* Internet Explorer/Edge */
    user-select: none;

    .fonted {
      font-family: "Helvetica Neue", Helvetica, "DejaVu Sans Light", Arial, sans-serif;
      color: #606266;
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
