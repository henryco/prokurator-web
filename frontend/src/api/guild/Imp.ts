import PrkGuildApi, {GuildFormData} from "@/api/guild/index";
import axios from "axios";

export default class GuildApiImp implements PrkGuildApi {

  async getGuildDetails(id: string | number): Promise<GuildFormData> {
    const r = await axios.get(`/api/protected/guild/${id}`)
    return r.data;
  }
}
