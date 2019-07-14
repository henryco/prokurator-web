<template>
  <div>
    <div>HOME</div>
    <div>OPEN: {{d_open}}}</div>
    <div>PROT: {{d_prot}}</div>
  </div>
</template>

<script>
  import axios from "axios"
  export default {
    name: "Main",

    data: () => ({
      d_open: null,
      d_prot: null
    }),

    mounted: async function() {
      const o = await axios.get("/api/open/ping")
      this.d_open = o.data

      const c = await axios.get("/api/protected/ping")
      this.d_prot = c.data
    }
  }
</script>

<style scoped type="scss">

</style>
