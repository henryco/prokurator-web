<template>
  <el-card :body-style="{ padding: '0px' }">

    <div class="hover-area">

      <div class="flip" v-if="d_loaded">
        <div class="flip-content">
          <slot name="flip"/>
        </div>
      </div>

      <el-image :src="image" class="image" @load="_loaded">
        <span slot="placeholder"><i class="el-icon-loading"></i></span>
      </el-image>
    </div>


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

  .hover-area:hover .flip {
    display: block !important;
  }

  .hover-area {
    position: relative;
    width: 100%;
    height: 100%;
    z-index: 0;
  }

  .flip {
    display: none;
    position: absolute;
    width: 100%;
    height: 100%;
    z-index: 2;

    .flip-content {
      position: relative;
      width: 100%;
      height: 100%;
    }
  }

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
