<template>
  <div class="prk-avatar">
    <el-avatar :style="c_avatarStyle" fit="cover" :src="icon"/>
    <slot>
      <span class="name">{{name}}</span>
    </slot>
    <slot name="right"/>
  </div>
</template>

<script lang="ts">
  import Vue from 'vue';

  declare interface State {
    d_loaded: boolean;
  }

  export default Vue.extend({
    name: "PrkAvatar",
    props: {
      icon: [String],
      name: [String],
      size: {
        type: String,
        default: '20px'
      }
    },

    computed: {
      c_avatarStyle: function () {
        return {
          minWidth: this.size,
          maxWidth: this.size,
          width: this.size,
          minHeight: this.size,
          maxHeight: this.size,
          height: this.size
        }
      }
    }
  });
</script>

<style scoped lang="scss">
  .prk-avatar {
    width: 100%;
    display: flex;
    flex-direction: row ;
    align-items: center;
    align-content: center;


    .name {
      text-align: left;
      flex-grow: 1;
      font-family: "Helvetica Neue", Helvetica, "DejaVu Sans Light", Arial, sans-serif;
      color: #909399;
      font-size: 12px;
      font-weight: bold;
      white-space: nowrap;
      overflow: hidden;
      text-overflow: ellipsis;
      margin-right: 8px;
      margin-left: 8px;
    }
  }
</style>
