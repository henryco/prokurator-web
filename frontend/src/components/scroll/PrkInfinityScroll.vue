<template>
  <div @scroll="_onScroll" ref="source" class="prk-infinity-scroll">
    <slot/>
  </div>
</template>

<script lang="ts">

  import {
    ActivatorProps,
    LoadEvent,
    INFINITY,
    EVENT
  } from "./index"

  import Vue from 'vue';

  declare interface Data {
    d_absolute: number; // [0; 1]
    d_pixels: number; // [0; inf] px
    d_calls: number;
    d_next: Function,
  }

  export default Vue.extend({
    name: "PrkInfinityScroll",

    props: {
      activator: [ActivatorProps, Object],

      next: {
        type: [Boolean],
        default: true
      },

      delay: {
        type: [Number, String],
        default: 200
      },

      retry: {
        type:  [Number, String],
        default: 1,
        validator: (v: any): boolean =>
          v === INFINITY || !Number.isNaN(Number(v))
      }
    },

    data: () => (<Data> {
      d_absolute: 0.8,
      d_pixels: 200,
      d_next: (() => {}),
      d_calls: 0
    }),

    computed: {
      c_limit: function (): number {
        return this.retry !== INFINITY
          ? Number(this.retry)
          : -1;
      }
    },

    methods: {
      _emit: function (event: LoadEvent) {
        this.$emit(EVENT, event)
      },

      _onBodyScroll: function (event: Event) {
        this._onScroll((<any>event.target).scrollingElement)
      },

      _onScroll: function (src: Element) {
        const max = src.scrollHeight - src.clientHeight

        const dif = max - src.scrollTop
        if (dif > this.d_pixels) return;

        const prc = src.scrollTop / max
        if (prc < this.d_absolute) return;

        const nextFunction: Function = (() => {
          this.d_next()
          this.d_next = (() => {})
        })

        if (Number.isNaN(prc) && dif === 0) {
          this.d_calls += 1

          this.d_next = (() => {
            setTimeout(() => {
              this.$nextTick(() => {
                if (this.c_limit !== -1 && this.d_calls > this.c_limit)
                  return;
                this._onScroll(src)
              })
            }, Number(this.delay))
          })

          if (this.next)
            nextFunction();

        } else {
          this.d_calls = 0
        }

        this._emit({
          next: nextFunction,
          absolute: prc,
          pixels: dif
        })
      }
    },

    watch: {
      activator: {
        immediate: true,
        handler: function (v: ActivatorProps) {
          if (v === null || v === undefined)
            return;
          if (v.absolute !== undefined)
            this.d_absolute = Math.max(0, Math.min(1, Number(v.absolute)));
          if (v.pixels !== undefined)
            this.d_pixels = Math.max(0, Number(v.pixels));
        }
      }
    },

    mounted(): void {
      window.addEventListener('scroll', this._onBodyScroll)
      this.$nextTick(() => this._onScroll((<Element>this.$refs.source)))
    },

    destroyed(): void {
      window.removeEventListener('scroll', this._onBodyScroll)
    }

  });
</script>

<style scoped lang="scss">
  .prk-infinity-scroll {
    overflow: auto;
  }
</style>
