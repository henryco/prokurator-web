<template>
  <div class="servers-view">
    <prk-infinity-scroll @fetch="load">
      <!--TODO-->
    </prk-infinity-scroll>
  </div>
</template>

<script lang="ts">
  import PrkInfinityScroll, {LoadEvent} from "@/components/scroll"
  import Vue from 'vue';

  export default Vue.extend({
    name: "Servers",

    components: {
      PrkInfinityScroll
    },

    data () {
      return {
        d_count: 0,
      }
    },
    methods: {
      load (event: LoadEvent): void {
        this.d_count += 1
      }
    }
  });
</script>

<style scoped lang="scss">

  .servers-view {
    position: relative;
    height: 100%;
    width: 100%;
  }


</style>
