import PrkGeneralApi, {GuildForm, UserForm} from "./PrkGeneralApi"
import axios from "axios"

export default class GeneralApi implements PrkGeneralApi {

  async getAvailableGuilds(): Promise<GuildForm[]> {
    const r = await axios.get("/api/protected/identity/guilds")
    return r.data;
  }

  async getUserInfo(): Promise<UserForm> {
    const r = await axios.get("/api/protected/identity/user")
    return r.data;
  }

}
