import PrkGuildApi, {DetailsEntity, GuildFormData} from "@/api/guild/index";

export default class MockGuildApi implements PrkGuildApi {

  async getGuildDetails(id: string | number): Promise<GuildFormData> {
    return <GuildFormData> {
      id: id,
      name: 'Chinskie baje',
      admin: true,
      icon: 'https://cdn.discordapp.com/icons/448453867814780930/95f695f6c3a481687633a0dafb678033.jpg'
    };
  }

  async fetchGuildMembers(id: string | number): Promise<DetailsEntity[]> {
    return [{
      id: '2343463234134',
      name: 'some guy'
    }];
  }

  async fetchGuildChannels(id: string | number, query?: string): Promise<DetailsEntity[]> {
    return [{
      id: '23454623434132',
      name: 'some channel'
    }];
  }



}
