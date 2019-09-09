<template>
  <div class="board-view">

    <div class="info" v-if="c_icon">
      <div class="avatar">
        <img :src="c_icon" alt="" class="avatar-icon"/>
      </div>
      <div class="banner-left">
        <!--TODO-->
      </div>
    </div>
    <div class="banner-left" v-else>
      <!--TODO-->
    </div>

    <div class="board">

      <div class="name">
        <h1 class="fonted">{{c_name}}</h1>
      </div>

      <board-search
        :admin="c_managed"
        @search="search"
        :fetch="filter"
        class="top"
      />
      <prk-infinity-scroll :next="false" @fetch="scrollEvent">
        <div class="image-container">
          <div v-for="i in 4" class="image-column">
            <div class="image-padding">
              <prk-image-card
                v-for="k of c_items.length"
                v-if="_inColumn(i - 1, k - 1, 4)"
                :key="c_items[k - 1].id"
                :image="c_items[k - 1].media.url"
                class="g-image"
              >
                <prk-avatar
                  :icon="c_items[k - 1].author.icon"
                  :name="c_items[k - 1].author.name"
                >
                  <span slot="right" class="g-date">
                    {{_dateFormat(c_items[k - 1].date)}}
                  </span>
                </prk-avatar>
              </prk-image-card>
            </div>
          </div>
        </div>
      </prk-infinity-scroll>
    </div>

    <div class="banner"><!--TODO--></div>
  </div>


</template>

<script lang="ts">
  import {Probe, Query, Page, Content, Details, Channel} from "@/api/media";
  import PrkInfinityScroll, {LoadEvent} from "@/components/scroll"
  import BoardSearch from "@/composites/search/BoardSearch.vue";
  import {ElLoadingComponent} from "element-ui/types/loading";
  import PrkImageCard from "@/components/card/PrkImageCard.vue";
  import PrkAvatar from "@/components/avatar/PrkAvatar.vue";
  import moment from "moment"
  import Vue from 'vue';

  const SIZE: number = 20;

  declare interface Guild {
    id: string;
    name: string;
    icon: string;
    managed: boolean;
  }

  declare interface State {
    d_loading?: ElLoadingComponent,
    d_items: Content[];
    d_guild?: Guild;
    d_query: Query;
    d_page: number;
  }

  export default Vue.extend({
    name: "Board",

    components: {
      PrkInfinityScroll,
      PrkImageCard,
      BoardSearch,
      PrkAvatar
    },

    data: () => (<State> {
      d_loading: undefined,
      d_guild: undefined,
      d_query: {},
      d_page: 0,
      d_items: []
    }),

    computed: {
      c_managed: function () {
        if (!this.d_guild) return false;
        return this.d_guild.managed
      },
      c_icon: function () {
        if (!this.d_guild) return;
        return this.d_guild.icon;
      },
      c_name: function () {
        if (!this.d_guild) return;
        return this.d_guild.name;
      },
      c_id: function () {
        return this.$route.params.id;
      },
      c_items: function (): Content[] {
        return this.d_items ? this.d_items : []
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
        }, this.c_id)

        if (this.d_loading) {
          this.d_loading.close()
          this.d_loading = undefined
        }

        this.d_items = content;
        return content;
      },

      filter: async function (c: string, q: string): Promise<Record<string, string>> {
        const guildId = this.$route.params.id;
        if (c === 'channel') {
          const channels = await this.api().guild.fetchGuildChannels(guildId, q);
          let obj: Record<string, string> = {}
          for (let m of channels) {
            obj[`${m.id}`] = m.name
          }
          return obj
        }
        if (c === 'user') {
          const members = await this.api().guild.fetchGuildMembers(guildId, q);
          let obj: Record<string, string> = {}
          for (let m of members) {
            obj[`${m.id}`] = m.name
          }
          return obj
        }

        return {}
      },

      search: async function (query?: Query) {
        console.log('search: ')
        console.dir(query)
        this.d_query = (query === undefined) ? {} : query;
        this.d_page = 0
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
      },

      _dateFormat: function (date: number | string): string {
        return moment(new Date(Number(date))).format('MM/DD/YYYY');
      },

      _inColumn: function (i: number, k: number, cols: number): boolean {
        return ((k - i) % cols == 0)
      }
    },

    mounted: async function () {
      this.startLoader();
      const guildId = this.$route.params.id;
      const data = await this.api().guild.getGuildDetails(guildId);
      this.d_guild = <Guild> {
        managed: data.admin,
        name: data.name,
        icon: data.icon,
        id: data.id
      }
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

    .fonted {
      font-family: "Helvetica Neue", Helvetica, "DejaVu Sans Light", Arial, sans-serif;
      color: #909399;
    }

    .banner {
      width: 100%;
      max-width: 100px;
    }

    .banner-left {
      width: 100%;
      max-width: 200px;
    }

    .info {
      width: 100%;
      max-width: 350px;
      min-width: 225px;
      padding-top: 48px;
      display: flex;
      flex-direction: column;
      align-items: center;
      align-content: center;

      .avatar {
        width: 55%;
        height: auto;
        max-height: 300px;
        max-width: 300px;

        .avatar-icon {
          width: 100% !important;
          height: auto !important;
          border-radius: 8% 26%;
          overflow: hidden;
          opacity: 0.8;
        }
      }
    }

    .board {
      width: 100%;
      min-width: 720px;

      .name {
        margin-top: 48px;
        h1 {
          font-size: 36px !important;
        }
      }

      .top {
        margin-top: 12px;
        margin-bottom: 25px;
      }
    }

    .image-container {
      width: 100%;
      display: flex;
      flex-direction: row;
      justify-content: center;
      flex-wrap: wrap;
      align-items: stretch;

      .image-column {
        max-width: 25%;
        width: 25%;
        /*min-width: 200px;*/
      }

      .image-padding {
        padding: 10px;
      }

      .g-image {
        width: 100%;
        height: auto;
        margin-top: 20px;
        margin-bottom: 20px;
      }

      .g-date {
        font-size: 13px;
      }
    }

  }
</style>
