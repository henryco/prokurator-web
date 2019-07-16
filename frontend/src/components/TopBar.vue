<template>
  <v-card flat>
    <v-toolbar color="primary" dark extended flat>

      <template v-slot:extension>
        <v-toolbar-title class="white--text">
          <!-- TODO -->
        </v-toolbar-title>
      </template>

      <v-spacer></v-spacer>

      <v-btn @click="onLogin" color="secondary" outline>
        LOGIN
      </v-btn>

    </v-toolbar>
  </v-card>
</template>

<script lang="ts">
  import Component from "vue-class-component"
  import Vue from "vue"

  @Component({
    name: "TopBar"
  }) export default class TopBar extends Vue {

    protected onLogin (): void {
      const router = this.$router.currentRoute
      const r_name = router.name

      this.$cookies.set('router_cache', r_name);
      window.location.href = "/login"
    }
  }
</script>

<style scoped lang="scss">
  .top-bar {
    position: relative;
  }
  .card--flex-toolbar {
    margin-top: -64px;
  }
</style>
