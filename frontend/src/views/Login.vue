<template>
  <div>LOGIN</div>
</template>

<script>
  import axios from 'axios';
  export default {
    name: "Login",

    mounted: async function () {
      const data = new FormData();
      data.set("discord_code", this.$route.query.code)

      const response = await axios.post("/api/auth/login", data, {
        headers: {'Content-Type': 'multipart/form-data' },
        withCredentials: true
      })

      const token = response.headers['authorization']
      this.$cookies.set('Authorization', token)

      if (!token)
        throw `Cannot authorize ${response}`
    }
  }
</script>

<style scoped type="scss">

</style>
