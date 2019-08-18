import PrkMediaApi, {Content, Probe} from ".";
import axios from "axios";

export default class MediaApi implements PrkMediaApi {

  async fetchMediaContent(probe: Probe, guild: string): Promise<Content[]> {
    return [];
  }

}
