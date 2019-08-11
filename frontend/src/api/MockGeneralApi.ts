import GeneralApi, {GuildForm, UserForm} from "./PrkGeneralApi"

const sleep = (ms: number) => {
  return new Promise(resolve => setTimeout(resolve, ms))
}

export default class MockGeneralApi implements GeneralApi {

  async getAvailableGuilds(): Promise<GuildForm[]> {
    await sleep(2000)
    return [
      {
        name: 'Dangerous Boy',
        id: '2334534534',
        icon: 'https://cdn.discordapp.com/icons/448453867814780930/95f695f6c3a481687633a0dafb678033.jpg',
        installed: true
      }
    ];
  }

  async getUserInfo(): Promise<UserForm> {
    return <UserForm> {
      name: 'Dangerous Boy',
      id: '2334534534',
      icon: 'https://cdn.discordapp.com/avatars/230748644117184513/3f8faf5a34ef03d7672d4840fa2687ad.png',
      permissions: []
    };
  }

}
