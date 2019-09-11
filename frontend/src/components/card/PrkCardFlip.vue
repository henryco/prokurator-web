<template>
  <div class="prk-card-flip">
    <div class="buttons">
      <el-button icon="el-icon-full-screen" class="card-button" circle @click="_open"></el-button>

      <el-dropdown style="margin-left: 10px" @command="_handleCommand">
        <el-button icon="el-icon-more-outline" class="card-button" circle></el-button>
        <el-dropdown-menu slot="dropdown">
          <el-dropdown-item icon="el-icon-document" command="details">Details</el-dropdown-item>
          <el-dropdown-item icon="el-icon-warning-outline" command="report">Report</el-dropdown-item>
        </el-dropdown-menu>
      </el-dropdown>

    </div>
  </div>
</template>

<script lang="ts">
  import Vue from 'vue';

  export default Vue.extend({
    name: "PrkCardFlip",

    methods: {
      _open: function () {
        this.$emit('open')
      },

      _handleCommand: function (command: string) {
        this.$emit(command)
      }
    }
  });
</script>

<style scoped lang="scss">
  .prk-card-flip {
    position: relative;
    width: 100%;
    height: 100%;
    background-color: rgba(255, 255, 255, .15);
    backdrop-filter: blur(5px);


    .buttons {
      display: flex;
      flex-direction: row;
      height: 100%;
      width: 100%;
      align-items: center;
      align-content: center;
      justify-content: center;
      flex-wrap: wrap;

      .card-button {
        padding: 15px !important;
        opacity: 0.65 !important;
        font-size: 15px !important;
      }
    }
  }
</style>
