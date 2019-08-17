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
         {{f.type.name}}: <b>{{f.value}}</b>
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
    </template>
  </el-autocomplete>
</template>

<script lang="ts">
  import {Context, Filter, Fetch, Query} from "./";
  import Vue from 'vue';

  declare interface State {
    d_context?: Context;
    d_filters: Filter[];
    d_state: string;
  }

  export default Vue.extend({
    name: "PrkSearchInput",

    props: {
      /**@type {string[]}*/
      context: [Array],

      /**@type {Fetch}*/
      fetch: Function
    },

    data: () => (<State> {
      d_context: undefined,
      d_filters: [],
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
      }
    },

    methods: {
      search: function () {
        this.$emit('search', <Query> {
          raw: this.d_state,
          filters: this.d_filters.map((f: Filter) => (<Context> {
            name: f.type.name,
            value: f.value
          }))
        })
      },

      suggestions: async function (query: string, cb: Function) {
        if (!this.d_context) {
          cb(this.context.filter((s: any) => s.includes(query))
            .map((v: unknown) => this.createContext(v))
          )
          return;
        }

        if (this.fetch) {
          cb(await this.fetch(this.d_context.name, query));
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
