import PrkMediaApi, {Content, Probe} from "./PrkMediaApi";
import axios from "axios";

export default class MediaApi implements PrkMediaApi {

  async fetchMediaContent(probe: Probe): Promise<Content[]> {
    return [];
  }

}
