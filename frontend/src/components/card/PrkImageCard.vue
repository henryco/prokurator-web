<template>
  <el-card :body-style="{ padding: '0px' }">

    <el-image :src="image" class="image" @load="_loaded">
      <span slot="placeholder">Loading...</span>
    </el-image>

    <div style="padding: 14px;" v-if="d_loaded" class="content">
      <slot/>
    </div>
  </el-card>
</template>

<script lang="ts">
  import Vue from 'vue';

  declare interface CState {
    d_loaded: boolean;
  }

  export default Vue.extend({
    name: "PrkImageCard",
    props: {
      image: [String]
    },

    data: () => (<CState> {
      d_loaded: false
    }),

    methods: {
      _loaded: function () {
        this.d_loaded = true
      }
    }
  });
</script>

<style scoped lang="scss">

  .content {
    display: flex;
    flex-direction: row;
  }

  .time {
    font-size: 13px;
    color: #999;
  }

  .bottom {
    margin-top: 13px;
    line-height: 12px;
  }

  .button {
    padding: 0;
    float: right;
  }

  .image {
    width: 100%;
    display: block;
  }

  .clearfix:before,
  .clearfix:after {
    display: table;
    content: "";
  }

  .clearfix:after {
    clear: both
  }
</style>
