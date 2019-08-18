<template>
  <prk-search-input :context="c_context" :fetch="fetch" @search="search"/>
</template>

<script lang="ts">
  import PrkSearchInput, {Context, Result, Query, DATE} from "@/components/search";
  import {Query as ApiQuery} from "@/api/media/PrkMediaApi";

  import Vue from 'vue';

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

    computed: {
      c_context: function () {
        return [
          'category',
          'channel',
          'author',
          'before',
          'after',
          'nsfw'
        ] // deleted also
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

      fetch: async function (context: string, query: string): Promise<Context[]> {
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
        return []
      },

      _channel: async function (query: string): Promise<Context[]> {
        return []
      },

      _user: async function (query: string): Promise<Context[]> {
        return []
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
