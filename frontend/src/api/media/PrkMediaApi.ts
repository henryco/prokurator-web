declare interface Page {
  page: number;
  size: number;
}

declare interface Probe {
  category?: string[];
  channel?: string[];
  user?: string[];
  before?: number;
  after?: number;
  nsfw?: boolean;
  page: Page;
}

declare interface Details {
  id: string;
  name: string;
  icon: string;
}

declare interface Channel {
  id: string;
  name: string;
  nsfw: boolean;
  guild: Details;
  category?: string;
}

declare interface Media {
  id: string;
  url: string;
  name: string;
  size: number;
  image: boolean;
}

declare interface Content {
  id: string;
  date: number;
  media: Media;
  author: Details;
  channel: Channel;
}

export {Probe, Page, Content, Details, Channel};

export default interface PrkMediaApi {

  fetchMediaContent(probe: Probe): Promise<Content[]>;
}

import MediaApi from "./MediaApi";
import Mock from "./MockMediaApi";
export {Mock, MediaApi}
