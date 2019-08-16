import PrkMediaApi, {Content, Probe} from "./PrkMediaApi";

export default class MockMediaApi implements PrkMediaApi {

  async fetchMediaContent(probe: Probe): Promise<Content[]> {
    return [

    ];
  }

}
