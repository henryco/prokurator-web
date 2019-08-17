<template>
  <el-date-picker
    value-format="timestamp"
    class="prk-hidden-date"
    @change="datePick"
    v-model="d_curr"
    type="date"
    ref="date"
  />
</template>

<script lang="ts">
  import Vue from 'vue';

  declare interface State {
    d_curr?: number;
  }

  export default Vue.extend({
    name: "PrkHiddenDate",

    data: () => (<State> {
      d_curr: undefined
    }),

    methods: {
      datePick: function (v: number): void {
        this.$nextTick(() => this.d_curr = undefined)
        this.$emit('date', v)
      },

      focus: function (): void {
        (<any>this.$refs.date).focus()
      }
    }

  });
</script>

<style lang="scss">
  .prk-hidden-date {
    width: 0 !important;
    height: 0 !important;
    opacity: 0 !important;
    * {
      width: 0 !important;
      height: 0 !important;
      opacity: 0 !important;
    }
  }
</style>
