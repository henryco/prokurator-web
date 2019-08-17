<template>
  <div class="servers-view">
    <prk-infinity-scroll @fetch="load" retry="0" class="content-container">
      <h1 class="title">{{strings.selectServer}}</h1>
      <div class="cards">
        <div @click="openCard(g)" v-for="g in d_guilds" :key="g.id" class="cardo">
          <el-card shadow="hover" class="hover-card">
            <div class="server-box">
              <el-avatar class="avatar" fit="cover" :src="g.icon" v-if="g.icon && g.icon !== 'null'"/>
              <el-avatar class="avatar" fit="cover" v-else><b>{{_textIcon(g.name)}}</b></el-avatar>
              <span class="server-name">{{g.name}}</span>
            </div>
          </el-card>
        </div>
      </div>
    </prk-infinity-scroll>
  </div>
</template>

<script lang="ts">
  import PrkInfinityScroll from "@/components/scroll"
  import {ElLoadingComponent} from "element-ui/types/loading";
  import {GuildForm} from "@/api/general/PrkGeneralApi"
  import Vue from 'vue';

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
        d_guilds: <GuildForm[]> [],
        d_loading: undefined,
      }
    },

    methods: {
      openCard: async function (guild: GuildForm) {
        if (!guild.installed) this.addGuild(guild.id)
        else this.openGuild(guild.id);
      },

      openGuild: function (id: string) {
        this.$router.push({
          params: {id: id},
          name: 'board'
        })
      },

      addGuild: function (id: string) {
        const win = window.open(`/auth/add?id=${id}`, '_blank');
        if (win === null) {
          this.$message({
            showClose: true,
            message: 'Oops, i cant open new window...',
            type: 'error'
          });
        } else win.focus();
      },

      load: async function () {
        const api = this.api().general;
        this.d_guilds = await api.getAvailableGuilds()

        if (this.d_loading) {
          this.d_loading.close()
          this.d_loading = undefined
        }
      },

      _textIcon: function (text?: string | null): string {
        if (!text) return '';
        return text.charAt(0);
      }
    },

    mounted(): void {
      this.d_loading = this.loadingService({
        fullscreen: true
      });
    }
  });
</script>

<style scoped lang="scss">

  .servers-view {
    position: relative;
    height: 100%;
    width: 100%;

    -webkit-touch-callout: none; /* iOS Safari */
    -webkit-user-select: none; /* Safari */
    -moz-user-select: none; /* Firefox */
    -ms-user-select: none; /* Internet Explorer/Edge */
    user-select: none;
  }

  .content-container {
    width: 100%;
    display: flex;
    flex-direction: column;
    align-content: center;
    align-items: center;
    justify-content: center;

    .title {
      padding-top: 4em;
      font-family: "Helvetica Neue", Helvetica, "DejaVu Sans Light", Arial, sans-serif;
      color: #606266;
      text-transform: uppercase;
    }

    .cards {
      display: inline-flex;
      flex-wrap: wrap;
      justify-content: center;
    }

    .cardo {
      margin: 12px;
      min-width: 300px;
      max-width: 400px;
      width: 30%;

      .hover-card {
        cursor: pointer;
        width: 100%;
        height: 100%;
      }

      .server-box {
        width: 100%;
        display: flex;
        flex-direction: row ;
        align-items: center;
        align-content: center;

        .avatar {
          min-width: 40px !important;
          min-height: 40px !important;
        }

        .server-name {
          text-align: center;
          flex-grow: 1;
          font-family: "Helvetica Neue", Helvetica, "DejaVu Sans Light", Arial, sans-serif;
          color: #909399;
          font-size: 18px;
          font-weight: bold;
          white-space: nowrap;
          overflow: hidden;
          text-overflow: ellipsis;
          margin-right: 8px;
          margin-left: 8px;
        }
      }
    }
  }

</style>
