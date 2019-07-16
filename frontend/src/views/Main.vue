<template>
  <div class="main">
    <v-card class="card--flex-toolbar">
      <v-toolbar card prominent>
        <v-toolbar-title class="body-2 grey--text">Title</v-toolbar-title>

        <v-spacer></v-spacer>

        <v-btn icon>
          <v-icon>search</v-icon>
        </v-btn>

        <v-btn icon>
          <v-icon>apps</v-icon>
        </v-btn>

        <v-btn icon>
          <v-icon>more_vert</v-icon>
        </v-btn>
      </v-toolbar>

      <v-divider></v-divider>

      <v-card-text style="height: 200px;"></v-card-text>
    </v-card>
  </div>
</template>

<script lang="ts">
  import Component from "vue-class-component"
  import Vue from "vue"

  @Component
  export default class Main extends Vue {

  }
</script>

<style scoped lang="scss">
  .main {
    position: relative;
    width: 100%;
    height: 100%;
  }

  .card--flex-toolbar {
    margin-top: -65px;
  }
</style>
