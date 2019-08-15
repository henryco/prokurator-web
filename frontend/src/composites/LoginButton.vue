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
      localStorage.setItem('router_cache', window.location.href)
      window.location.href = "/login"
    }
  }
</script>
