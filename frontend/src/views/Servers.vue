<template>
  <div class="servers-view">
    <prk-infinity-scroll @fetch="load">
      <div v-for="g in d_guilds" :key="g.id">
        {{g}}
      </div>
    </prk-infinity-scroll>
  </div>
</template>

<script lang="ts">
  import PrkInfinityScroll, {LoadEvent} from "@/components/scroll"
  import {ElLoadingComponent} from "element-ui/types/loading";
  import {GeneralApi, GuildForm} from "@/api/PrkGeneralApi"
  import {Loading} from 'element-ui';

  import Vue from 'vue';

  const api = new GeneralApi();

  declare interface Data {
    d_loading?: ElLoadingComponent,
    d_guilds: GuildForm[]
  }

  export default Vue.extend({
    name: "Servers",

    components: {
      PrkInfinityScroll
    },

    data(): Data {
      return {
        d_loading: undefined,
        d_guilds: [],
      }
    },

    methods: {
      async load(event: LoadEvent) {
        this.d_guilds = await api.getAvailableGuilds()

        if (this.d_loading) {
          this.d_loading.close()
          this.d_loading = undefined
        }
      }
    },

    mounted(): void {
      this.d_loading = Loading.service({fullscreen: true});
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
