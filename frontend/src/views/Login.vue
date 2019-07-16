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

      const lastPath = this.$cookies.get("router_cache");
      if (lastPath) {
        this.$cookies.remove("router_cache");
        this.$router.push({name: lastPath});
      }
    }
  }
</script>
