import GeneralApi, {GuildForm, UserForm} from "./PrkGeneralApi"

const sleep = (ms: number) => {
  return new Promise(resolve => setTimeout(resolve, ms))
}

export default class MockGeneralApi implements GeneralApi {

  async getAvailableGuilds(): Promise<GuildForm[]> {
    await sleep(2000)
    return [
      {
        name: 'Chinskie bajki',
        id: '2994534534',
        icon: 'https://cdn.discordapp.com/icons/448453867814780930/95f695f6c3a481687633a0dafb678033.jpg',
        installed: true
      },
      {
        name: 'Chinskie bajki',
        id: '2334515434',
        icon: 'https://cdn.discordapp.com/icons/448453867814780930/95f695f6c3a481687633a0dafb678033.jpg',
        installed: true
      },
      {
        name: 'Test 12354',
        id: '2334557334',
        icon: 'https://cdn.discordapp.com/icons/448453867814780930/95f695f6c3a481687633a0dafb678033.jpg',
        installed: false
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
