<template>
  <prk-search-input :context="c_context" :fetch="_fetch" @search="search"/>
</template>

<script lang="ts">
  import PrkSearchInput, {Context, Result, Query, DATE} from "@/components/search";
  import {Query as ApiQuery} from "@/api/media/PrkMediaApi";

  type FetchFunction = (context: string, query: string) => Promise<Record<string, string>>;

  import Vue from 'vue';

  async function _map(r?: Record<string, string>): Promise<Context[]> {
    let results: Context[] = [];
    if (r) {
      for (let k in r) {
        if (r.hasOwnProperty(k)) {
          const id = k;
          const v = r[k];
          const e = id === v ? '' : `(${id})`;
          results.push({
            label: `${v} ${e}`,
            value: k,
            name: k
          });
        }
      }
    }
    return results;
  }

  function _filter (s: string, m: Result[]): string[] | undefined {
    const r = m.filter(m => m.name === s).map(r => r.value)
    if (r.length === 0) return;
    return [...new Set(r)];
  }

  function _date(s: string, m: Result[]): number | undefined {
    const r = _filter(s, m)
    if (r === undefined) return;
    return Number(r[0])
  }

  export default Vue.extend({
    name: "BoardSearch",

    components: {
      PrkSearchInput
    },

    props: {
      /**@type {FetchFunction}*/
      fetch: Function,

      admin: [Boolean],
    },

    computed: {
      c_context: function () {
        let arr = ['category', 'channel', 'author', 'before', 'after', 'nsfw']
        if (this.admin) arr.push('deleted');
        return arr;
      }
    },

    methods: {
      search: function (query: Query) {
        const filters = query.filters
        this.$emit('search', <ApiQuery> {
          category: _filter('category', filters),
          deleted: _filter('deleted', filters),
          channel: _filter('channel', filters),
          user: _filter('author', filters),
          before: _date('before', filters),
          after: _date('after', filters),
          nsfw: _filter('nsfw', filters),
          raw: query.raw
        });
      },

      _fetch: async function (context: string, query: string): Promise<Context[]> {
        return (<Record<string, Function>>{
          "category": this._category,
          "channel": this._channel,
          "deleted": this._bool,
          "author": this._user,
          "before": this._date,
          "after": this._date,
          "nsfw": this._bool
        })[`${context}`](query)
      },

      _category: async function (query: string): Promise<Context[]> {
        const r: Record<string, string> = await this.fetch('category', query);
        return _map(r);
      },

      _channel: async function (query: string): Promise<Context[]> {
        const r: Record<string, string> = await this.fetch('channel', query);
        return _map(r);
      },

      _user: async function (query: string): Promise<Context[]> {
        const r: Record<string, string> = await this.fetch('user', query);
        return _map(r);
      },

      _bool: async function (query: string) {
        return <Context[]>(["true", "false"]
          .filter(s => s.includes(query))
          .map(v => (<Context> {
            value: v,
            name: v
          }))
        )
      },

      _date: async function (){
        return <Context[]> [DATE]
      }
    }

  });
</script>
