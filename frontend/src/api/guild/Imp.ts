import PrkGuildApi, {DetailsEntity, GuildFormData} from "@/api/guild/index";
import axios from "axios";

export default class GuildApiImp implements PrkGuildApi {

  async getGuildDetails(id: string | number): Promise<GuildFormData> {
    const r = await axios.get(`/api/protected/guild/${id}`)
    return r.data;
  }

  async fetchGuildMembers(id: string | number, query?: string): Promise<DetailsEntity[]> {
    const q = (query && query.length > 0) ? `?query=${query}` : ""
    const r = await axios.get(`/api/protected/guild/${id}/users${q}`)
    return r.data;
  }

  async fetchGuildChannels(id: string | number, query?: string): Promise<DetailsEntity[]> {
    const q = (query && query.length > 0) ? `?query=${query}` : ""
    const r = await axios.get(`/api/protected/guild/${id}/channels${q}`)
    return r.data;
  }

}
