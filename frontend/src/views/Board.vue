<template>
  <div class="board-view">

    <div class="top">
      board: {{c_boardId}}
    </div>

    <prk-infinity-scroll :next="false" @fetch="load">
      <div v-for="i in d_count">
        {{i}}
      </div>
    </prk-infinity-scroll>

  </div>
</template>

<script lang="ts">
  import {ElLoadingComponent} from "element-ui/types/loading";
  import PrkInfinityScroll, {LoadEvent} from "@/components/scroll"

  import Vue from 'vue';

  declare interface Data {
    d_loading?: ElLoadingComponent,
    d_count: number;
  }

  export default Vue.extend({
    name: "Board",

    components: {
      PrkInfinityScroll
    },

    data: () => (<Data> {
      d_loading: undefined,
      d_count: 0
    }),

    computed: {
      c_boardId: function () {
        return this.$route.params.id
      }
    },

    methods: {
      load: async function (event: LoadEvent) {
        this.d_count += 1;
        if (this.d_loading) {
          this.d_loading.close()
          this.d_loading = undefined
        }

        event.next();
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
