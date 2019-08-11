interface IdEntity {
  icon?: string;
  name: string;
  id: string;
}

interface GuildForm extends IdEntity {
  installed: boolean;
}

interface UserForm extends IdEntity {
  permissions: string[];
}

export {IdEntity, GuildForm, UserForm};

export default interface PrkGeneralApi {

  getAvailableGuilds(): Promise<GuildForm[]>;

  getUserInfo(): Promise<UserForm>;
}

import Mock from "./MockGeneralApi";
export {Mock, Mock as GeneralApi};

