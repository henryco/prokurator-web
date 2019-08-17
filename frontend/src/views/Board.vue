<template>
  <div class="board-view">
    <div class="banner"><!--TODO--></div>

    <div class="board">
      <board-search class="top" @search="search"/>
      <prk-infinity-scroll :next="false" @fetch="scrollEvent">
        <div v-for="item of d_items" :key="item.id">
          {{item}}
        </div>
      </prk-infinity-scroll>
    </div>

    <div class="banner"><!--TODO--></div>
  </div>


</template>

<script lang="ts">
  import {Probe, Query, Page, Content, Details, Channel} from "@/api/media/PrkMediaApi";
  import PrkInfinityScroll, {LoadEvent} from "@/components/scroll"
  import BoardSearch from "@/composites/search/BoardSearch.vue";
  import {ElLoadingComponent} from "element-ui/types/loading";
  import Vue from 'vue';

  const SIZE: number = 20;
  declare interface Data {
    d_loading?: ElLoadingComponent,
    d_items: Content[];
    d_query?: Query;
    d_page: number;
  }

  export default Vue.extend({
    name: "Board",

    components: {
      PrkInfinityScroll,
      BoardSearch
    },

    data: () => (<Data> {
      d_loading: undefined,
      d_query: undefined,
      d_page: 0,
      d_items: []
    }),

    computed: {
      c_boardId: function () {
        return this.$route.params.id
      }
    },

    methods: {
      load: async function (): Promise<Content[]> {
        // noinspection UnnecessaryLocalVariableJS
        const content = await this.api().media.fetchMediaContent({
          query: this.d_query,
          page: {
            page: this.d_page++,
            size: SIZE
          }
        })

        if (this.d_loading) {
          this.d_loading.close()
          this.d_loading = undefined
        }

        this.d_items = content;
        return content;
      },

      search: async function (query?: Query) {
        this.d_query = query;
        this.startLoader();
        await this.load();
      },

      scrollEvent: async function (event: LoadEvent) {
        const content = await this.load();
        if (content.length === SIZE)
          event.next();
      },

      startLoader: function () {
        this.d_loading = this.loadingService({
          fullscreen: true
        })
      }
    },

    mounted(): void {
      this.startLoader();
    }
  });
</script>

<style scoped lang="scss">
  .board-view {
    position: relative;
    width: 100%;
    display: flex;
    flex-direction: row;
    overflow-x: hidden;

    .banner {
      width: 100%;
      max-width: 250px;
    }

    .board {
      width: 100%;
      min-width: 720px;

      .top {
        margin-top: 5%;
        margin-bottom: 2.5%;
      }
    }

  }
</style>
