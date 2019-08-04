<template>
  <el-container class="prk-top-bar">
    <el-header class="prk-header">
      <div class="prk-sub prk-left"><slot name="left"/></div>
      <div class="prk-sub prk-right"><slot name="right"/></div>
    </el-header>
  </el-container>
</template>

<script lang="ts">
  import Vue from 'vue';

  export default Vue.extend({
    name: "PrkTopBar"
  });
</script>

<style scoped lang="scss">
  .prk-top-bar {
    position: relative;
    width: 100%;
    height: 100%;
  }

  .prk-header {
    position: relative;
    width: 100%;
    height: 100% !important;
    min-height: 60px !important;
    display: flex;
    flex-direction: row;
    align-items: center;
    align-content: center;
    border-bottom: 1px solid #dcdfe6;
    justify-content: space-between;

    .prk-sub {
      display: flex;
      flex-direction: row;
      align-content: center;
      align-items: center;
    }

    .prk-left {
      margin-left: 16px;
    }
    .prk-right {
      margin-right: 16px;
    }
  }
</style>
