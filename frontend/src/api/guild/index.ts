declare interface GuildFormData {
  id: string;
  name: string;
  icon: string;
  admin: boolean;
}

export {GuildFormData};

export default interface PrkGuildApi {
  getGuildDetails(id: string | number): Promise<GuildFormData>;
}

import MockGuildApi from "./Mock"
import GuildApiImp from "./Imp"

export {MockGuildApi, GuildApiImp};
