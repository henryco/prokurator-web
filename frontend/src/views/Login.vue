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

      axios({
        method: 'post',
        url: '/login',
        data: data,
        config: {
          headers: {'Content-Type': 'multipart/form-data' },
          withCredentials: true
        }
      })
      .then(function (response) {
        //handle success
        console.log(response);
      })
      .catch(function (response) {
        //handle error
        console.log(response);
      });
    }
  }
</script>

<style scoped type="scss">

</style>
