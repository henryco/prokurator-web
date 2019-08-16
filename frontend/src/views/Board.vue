<template>
  <div class="board-view">

    <div class="top">
      board: {{c_boardId}}
    </div>

    <prk-infinity-scroll :next="false" @fetch="load">
      <div v-for="item of d_items" :key="item.id">

      </div>
    </prk-infinity-scroll>

  </div>
</template>

<script lang="ts">
  import {Probe, Page, Content, Details, Channel} from "@/api/media/PrkMediaApi";
  import PrkInfinityScroll, {LoadEvent} from "@/components/scroll"
  import {ElLoadingComponent} from "element-ui/types/loading";

  import Vue from 'vue';

  const SIZE: number = 20;
  declare interface Data {
    d_loading?: ElLoadingComponent,
    d_items: Content[];
    d_page: number;
  }

  export default Vue.extend({
    name: "Board",

    components: {
      PrkInfinityScroll
    },

    data: () => (<Data> {
      d_loading: undefined,
      d_items: [],
      d_page: 0
    }),

    computed: {
      c_boardId: function () {
        return this.$route.params.id
      }
    },

    methods: {
      load: async function (event: LoadEvent) {

        // noinspection UnnecessaryLocalVariableJS
        const content = await this.api().media.fetchMediaContent({
          page: {
            page: this.d_page++,
            size: SIZE
          }
        })
        this.d_items = content;

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
