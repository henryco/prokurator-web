<template>
  <div class="board-view">

    <div class="top">
      board: {{c_boardId}}
    </div>

    <prk-infinity-scroll @fetch="load">

    </prk-infinity-scroll>

  </div>
</template>

<script lang="ts">
  import {ElLoadingComponent} from "element-ui/types/loading";
  import PrkInfinityScroll from "@/components/scroll"

  import Vue from 'vue';

  declare interface Data {
    d_loading?: ElLoadingComponent,
  }

  export default Vue.extend({
    name: "Board",

    components: {
      PrkInfinityScroll
    },

    data: () => (<Data> {
      d_loading: undefined
    }),

    computed: {
      c_boardId: function () {
        return this.$route.params.id
      }
    },

    methods: {
      async load() {

        if (this.d_loading) {
          this.d_loading.close()
          this.d_loading = undefined
        }
      }
    },

    mounted(): void {
      this.d_loading = this.loadingService({
        fullscreen: true
      })
    }
  });
</script>

<style scoped lang="scss">
  .board-view {
    position: relative;
    width: 100%;
    height: 100%;
  }
</style>
