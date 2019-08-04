<template>
  <el-container class="main-view">
    <top-bar/>
  </el-container>
</template>

<script lang="ts">

  import TopBar from "@/composites/TopBar.vue";

  import Vue from 'vue'

  export default Vue.extend({
    name: 'Main',
    components: {
      TopBar
    }
  })
</script>

<style scoped lang="scss">
  .main-view {
    position: relative;
    width: 100%;
    height: 100%;
  }
</style>
