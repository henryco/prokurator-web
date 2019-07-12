<template>
  <div>LOGIN</div>
</template>

<script>
  export default {
    name: "Login",

    mounted () {
      const code = this.$route.query.code;
      console.log(`code: ${code}`)
    }
  }
</script>

<style scoped type="scss">

</style>
