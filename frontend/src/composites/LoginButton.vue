<template>
  <el-button @click="onLogin" icon="el-icon-user">
    <slot>Login</slot>
  </el-button>
</template>

<script lang="ts">
  import Component from "vue-class-component"
  import Vue from "vue"

  @Component({
    name: "LoginButton"
  }) export default class LoginButton extends Vue {

    protected onLogin (): void {
      const r_name = this.$router.currentRoute.name
      localStorage.setItem('router_cache', `${r_name}`)
      window.location.href = "/login"
    }
  }
</script>
