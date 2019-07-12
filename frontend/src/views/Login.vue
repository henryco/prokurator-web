<template>
  <div>LOGIN</div>
</template>

<script>
  import axios from 'axios';
  export default {
    name: "Login",

    mounted: async function () {
      const code = this.$route.query.code;
      console.log(`code: ${code}`)

      const data = new FormData();
      data.set("discord_code", code)

      const response = await axios.post("http://localhost:8080/login", data, {
        headers: {'Content-Type': 'multipart/form-data' },
        withCredentials: true
      })

      console.log(response)
    }
  }
</script>

<style scoped type="scss">

</style>
