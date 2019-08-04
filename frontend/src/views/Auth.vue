<script lang="ts">
  import { Loading } from 'element-ui';
  import axios from 'axios'
  import Vue from 'vue'

  export default Vue.extend({
    name: 'Auth',
    mounted: async function () {
      const data = new FormData()
      const code = this.$route.query.code
      if (code === null || code === undefined)
        throw 'Cannot fetch login token code from uri param'

      const loading = Loading.service({ fullscreen: true });

      data.set('discord_code', `${code}`)
      const response = await axios.post('/api/auth/login', data, {
        headers: {'Content-Type': 'multipart/form-data'},
        withCredentials: true
      })

      const token = response.headers['authorization']
      localStorage.setItem('Authorization', token)

      if (!token)
        throw `Cannot authorize ${response}`

      this.$nextTick(() => loading.close());

      const lastPath = localStorage.getItem('router_cache')
      if (lastPath) {
        localStorage.removeItem('router_cache')
        this.$router.push({name: lastPath})
      } else {
        this.$router.push({name: 'main'})
      }
    }
  })
</script>
