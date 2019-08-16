<template>
  <el-button @click="onLogin" icon="el-icon-user">
    <slot>Login</slot>
  </el-button>
</template>

<script lang="ts">
  import Component from "vue-class-component"
  import Vue from "vue"

  @Component({
    name: "composites.top.LoginButton"
  }) export default class LoginButton extends Vue {

    protected onLogin (): void {
      this.$emit('login')
    }
  }
</script>
