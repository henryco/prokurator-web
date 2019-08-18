<template>
  <el-autocomplete
    class="input-with-select prk-search-input"
    :fetch-suggestions="suggestions"
    @keypress.enter.native="search"
    placeholder="Please input"
    @select="handleSelect"
    v-model="d_state"
  >
    <el-button slot="append" icon="el-icon-search" @click="search"/>

    <template slot="prepend">
      <template v-for="f in d_filters">
        <el-tag
          class="prk-search-tag"
          @close="removeFilter(f)"
          disable-transitions
          effect="plain"
          size="medium"
          type="info"
          closable
        >
         {{f.type.name}}: <b>{{ f.name === undefined ? f.value : f.name}}</b>
        </el-tag>
      </template>
      <el-tag
        class="prk-search-tag"
        @close="clearContext"
        disable-transitions
        v-if="d_context"
        effect="plain"
        size="medium"
        closable
      >
        {{d_context.value}}
      </el-tag>
      <prk-hidden-date
        @date="dateSelect"
        ref="datepicker"
        v-if="d_date"
      />
    </template>
  </el-autocomplete>
</template>

<script lang="ts">
  import PrkHiddenDate from "@/components/search/PrkHiddenDate.vue";
  import {Context, Filter, Fetch, Query, Result, DATE} from "./";
  import Vue from 'vue';

  declare interface State {
    d_context?: Context;
    d_filters: Filter[];
    d_state: string;
    d_date: boolean;
  }

  export default Vue.extend({
    name: "PrkSearchInput",

    components: {
      PrkHiddenDate
    },

    props: {
      /**@type {string[]}*/
      context: [Array],

      /**@type {Fetch}*/
      fetch: Function
    },

    data: () => (<State> {
      d_context: undefined,
      d_filters: [],
      d_date: false,
      d_state: ''
    }),

    watch: {
      d_state: function (v: string) {
        if (this.d_context) return;
        for (const c of this.context) {
          if (v === `${c}:`) {
            this.d_context = this.createContext(c);
            this.d_state = '';
            return;
          }
        }
      },
      d_context: function (v?: Context) {
        if (v === undefined)
          this.d_date = false;
      }
    },

    methods: {
      search: function () {
        this.d_context = undefined;
        this.$emit('search', <Query> {
          raw: this.d_state,
          filters: this.d_filters.map((f: Filter) => (<Result> {
            name: f.type.name,
            value: f.value
          }))
        })
      },

      suggestions: async function (query: string, cb: Function) {
        if (!this.d_context) {
          this.d_date = false;
          cb(this.context.filter((s: any) => s.includes(query))
            .map((v: unknown) => this.createContext(v))
          )
          return;
        }

        if (this.fetch) {
          const context = await this.fetch(this.d_context.name, query)
          if (context.length === 1 && context[0] === DATE) {
            this.d_date = true
            this.startDateFocusQueue();
            cb([]);
            return;
          }
          this.d_date = false;
          cb(context);
        }
      },

      createContext: (v: any): Context => ({
        value: `${v}`.replace(":", "") + ":",
        name: `${v}`
      }),

      removeFilter: function (filter: Filter): void {
        const index = this.d_filters.indexOf(filter)
        if (index !== -1) this.d_filters.splice(index, 1);
      },

      clearContext: function (): void {
        this.d_context = undefined;
      },

      handleSelect: function (item: Context): void {
        this.d_state = '';
        if (!this.d_context) {
          this.d_context = item;
          return;
        }
        const context = this.d_context
        this.d_context = undefined;
        this.d_filters.push(<Filter> {
          value: item.value,
          type: context
        });
      },

      dateSelect: function (date: number): void {
        if (!this.d_date || !this.d_context) return;
        const context = this.d_context
        this.d_context = undefined;
        this.d_state = '';
        this.d_filters.push(<Filter> {
          name: new Date(date).toLocaleDateString(),
          type: context,
          value: date
        })
      },

      startDateFocusQueue: function (): void {
        if (!this.d_date) return;

        const date = (<any>this.$refs.datepicker)
        if (date !== undefined) {
          date.focus()
          return;
        }

        setTimeout(() => {
          this.$nextTick(() => {
            this.startDateFocusQueue();
          })
        }, 20)
      }

    }

  });
</script>

<style lang="scss">
  .prk-search-input {
    width: 100% !important;

    .prk-search-tag {
      margin: 0 3px !important;
    }

    .el-input-group__prepend {
      background-color: transparent !important;
      padding: 0 3px !important;
    }
  }
</style>
