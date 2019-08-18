import PrkGuildApi, {GuildFormData} from "@/api/guild/index";

export default class MockGuildApi implements PrkGuildApi {

  async getGuildDetails(id: string | number): Promise<GuildFormData> {
    return <GuildFormData> {
      id: id,
      name: 'Chinskie baje',
      admin: true,
      icon: 'https://cdn.discordapp.com/icons/448453867814780930/95f695f6c3a481687633a0dafb678033.jpg'
    };
  }
}
