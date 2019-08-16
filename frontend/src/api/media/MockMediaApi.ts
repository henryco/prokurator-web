import PrkMediaApi, {Probe, Page, Content, Details, Channel} from "./PrkMediaApi";

export default class MockMediaApi implements PrkMediaApi {

  async fetchMediaContent(probe: Probe): Promise<Content[]> {
    await new Promise(r => setTimeout(r, 1500));
    return [

    ];
  }

}
