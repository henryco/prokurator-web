import PrkMediaApi, {Probe, Page, Content, Details, Channel} from "./";

export default class MockMediaApi implements PrkMediaApi {

  async fetchMediaContent(probe: Probe, guild: string): Promise<Content[]> {
    await new Promise(r => setTimeout(r, 1500));
    return [
      {
        id: "123",
        date: 0,
        deleted: false,
        author: {
          id: "12345",
          name: "Someone",
          icon: ""
        },
        media: {
          id: "wesdfbdf",
          name: "Wut.png",
          size: 1423546,
          image: true,
          url: ""
        },
        channel: {
          id: "2345346546",
          name: "random",
          nsfw: false,
          guild: {
            id: "sdfbertb34345",
            name: "Some guild",
            icon: "dfgdfb.png"
          }
        }
      },
    ];
  }

}
