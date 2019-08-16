interface IdEntity {
  icon?: string | null;
  name?: string | null;
  id: string;
}

interface GuildForm extends IdEntity {
  installed: boolean;
}

interface UserForm extends IdEntity {
}

export {IdEntity, GuildForm, UserForm};

export default interface PrkGeneralApi {

  getAvailableGuilds(): Promise<GuildForm[]>;

  getUserInfo(): Promise<UserForm>;
}

import GeneralApi from "./GeneralApi"
import Mock from "./MockGeneralApi";
export {Mock, GeneralApi};
