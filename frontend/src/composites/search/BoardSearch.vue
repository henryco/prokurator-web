<template>
  <prk-search-input :context="c_context" :fetch="fetch" @search="search"/>
</template>

<script lang="ts">
  import PrkSearchInput, {Context, Query, DATE} from "@/components/search";
  import {Query as ApiQuery} from "@/api/media/PrkMediaApi";

  import Vue from 'vue';

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
          'user',
          'before',
          'after',
          'nsfw'
        ] // deleted also
      }
    },

    methods: {
      search: function (query: Query) {
        console.dir(query)
        this.$emit('search', <ApiQuery> {
        //  TODO: MAP
        });
      },

      fetch: async function (context: string, query: string): Promise<Context[]> {
        if (context === 'nsfw') return [
          {value: 'true', name: 'true'},
          {value: 'false', name: 'false'}
        ]

        if (context === 'before' || context === 'after') {
          return [DATE]
        }

        return []
      }
    }

  });
</script>
