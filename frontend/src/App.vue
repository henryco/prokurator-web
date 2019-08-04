<template>
  <div id="app">
    <router-view/>
  </div>
</template>

<script lang="ts">
  import AuthRequestMixin from '@/util/AuthRequestMixin'
  import Vue from 'vue'

	export default Vue.extend({
		name: 'app',
    mixins: [AuthRequestMixin]
	})
</script>

<style lang="scss">
  body {
    margin-top: 0 !important;
    margin-bottom: 0 !important;
  }
</style>
