declare interface Page {
  page: number;
  size: number;
}

declare interface Query {
  category?: string[];
  channel?: string[];
  deleted?: boolean;
  user?: string[];
  before?: number;
  after?: number;
  nsfw?: boolean;
}

declare interface Probe {
  query?: Query;
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
  deleted: boolean;
}

export {Probe, Query, Page, Content, Details, Channel};

export default interface PrkMediaApi {

  fetchMediaContent(probe: Probe): Promise<Content[]>;
}

import MediaApi from "./MediaApi";
import Mock from "./MockMediaApi";
export {Mock, MediaApi}
