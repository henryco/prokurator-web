declare interface GuildFormData {
  id: string;
  name: string;
  icon?: string;
  admin: boolean;
}

declare interface DetailsEntity {
  id: string;
  name: string;
}

export {GuildFormData, DetailsEntity};

export default interface PrkGuildApi {
  getGuildDetails(id: string | number): Promise<GuildFormData>;

  fetchGuildMembers(id: string | number, query?: string): Promise<DetailsEntity[]>;

  fetchGuildChannels(id: string | number, query?: string): Promise<DetailsEntity[]>;
}

import MockGuildApi from "./Mock"
import GuildApiImp from "./Imp"

export {MockGuildApi, GuildApiImp};
