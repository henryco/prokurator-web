<template>
  <div id="app">
    <div class="top-navigation">
      <top-bar class="bar" :authorized="isAuthorized()" />
    </div>
    <div class="router-box">
      <router-view/>
    </div>
  </div>
</template>

<script lang="ts">
  import LoaderMixin from "@/utils/LoaderMixin";
  import I8NMixin from "@/i8n"
  import ApiMixin from '@/api'
  import TopBar from "@/composites/top/TopBar.vue";
  import Vue from 'vue'

	export default Vue.extend({
		name: 'app',
    mixins: [LoaderMixin, ApiMixin, I8NMixin],
    components: {TopBar}
  })
</script>

<style lang="scss">
  body {
    margin-top: 0 !important;
    margin-bottom: 0 !important;
  }

  #app {
    position: absolute;
    left: 0;
    right: 0;
    top: 0;
    bottom: 0;

    .top-navigation {
      position: fixed;
      top: 0;
      right: 0;
      left: 0;
      z-index: 1;
      background-color: white;
      height: 80px;

      .bar {
        padding-right: 12px;
        padding-left: 12px;
      }
    }

    .router-box {
      width: 100%;
      position: relative;
      padding-top: 80px;
      height: calc(100% - 80px);
    }
  }
</style>
