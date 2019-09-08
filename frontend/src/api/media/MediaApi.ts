import PrkMediaApi, {Content, Probe} from ".";
import axios from "axios";

export default class MediaApi implements PrkMediaApi {

  async fetchMediaContent(probe: Probe, guild: string): Promise<Content[]> {

    // default behavior on deleted items
    if (probe.query.deleted === undefined || probe.query.deleted.length === 0) {
      probe.query.deleted = [false]
    }

    // default behavior on nsfw items
    if (probe.query.nsfw === undefined || probe.query.nsfw.length === 0) {
      probe.query.nsfw = [false]
    }

    const r = await axios.post(`/api/protected/media/${guild}`, probe)
    return r.data
  }

}
