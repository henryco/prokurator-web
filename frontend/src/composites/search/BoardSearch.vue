<template>
  <prk-search-input
    :context="c_context"
    :fetch="fetch"
    @search="search"
  />
</template>

<script lang="ts">
  import PrkSearchInput, {Context, Query} from "@/components/search";

  import Vue from 'vue';

  export default Vue.extend({
    name: "BoardSearch",

    components: {
      PrkSearchInput
    },

    computed: {
      c_context: function () {
        return ['deleted', 'nsfw', 'author', 'channel']
      }
    },

    methods: {
      search: function (query: Query) {
        this.$emit('search', query);
      },

      fetch: async function (context: string, query: string): Promise<Context[]> {
        if (context === 'nsfw') return [
          {value: 'true', name: 'true'},
          {value: 'false', name: 'false'}
        ]
        return []
      }
    }

  });
</script>
