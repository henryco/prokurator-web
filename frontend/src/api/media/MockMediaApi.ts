import PrkMediaApi, {Probe, Content} from "./";

export default class MockMediaApi implements PrkMediaApi {

  async fetchMediaContent(probe: Probe, guild: string): Promise<Content[]> {
    await new Promise(r => setTimeout(r, 1500));
    return [{ "id": "620313435888025604", "date": 1567900800000, "media": { "id": "fde6ac8e-29c5-43ec-8935-2fea80b679f4", "url": "http://174.138.0.194:34123/api/storage/get/fde6ac8e-29c5-43ec-8935-2fea80b679f4", "name": "unknown.png", "size": 3574317, "image": true }, "author": { "id": "230748644117184513", "name": "dangerous boy", "icon": "https://cdn.discordapp.com/avatars/230748644117184513/402476fe9dda0517d7d5542479d9b281.png" }, "channel": { "id": "454679745842642944", "name": "pogaduszki", "nsfw": false, "guild": "448453867814780930", "category": "Weeb stuff" }, "deleted": false },
      { "id": "620305639091994625", "date": 1567900800000, "media": { "id": "7885d1cf-e9c7-44d3-9aaf-18bb69c1a390", "url": "http://174.138.0.194:34123/api/storage/get/7885d1cf-e9c7-44d3-9aaf-18bb69c1a390", "name": "unknown.png", "size": 950168, "image": true }, "author": { "id": "292789928247689216", "name": "Killeras", "icon": "https://cdn.discordapp.com/avatars/292789928247689216/0e88751d9be61cbe7f75dc6491b7d43d.png" }, "channel": { "id": "454679745842642944", "name": "pogaduszki", "nsfw": false, "guild": "448453867814780930", "category": "Weeb stuff" }, "deleted": false },
      { "id": "620305033124380722", "date": 1567900800000, "media": { "id": "557df90b-517a-4284-8af2-d7df0eefbaab", "url": "http://174.138.0.194:34123/api/storage/get/557df90b-517a-4284-8af2-d7df0eefbaab", "name": "unknown.png", "size": 966005, "image": true }, "author": { "id": "292789928247689216", "name": "Killeras", "icon": "https://cdn.discordapp.com/avatars/292789928247689216/0e88751d9be61cbe7f75dc6491b7d43d.png" }, "channel": { "id": "454679745842642944", "name": "pogaduszki", "nsfw": false, "guild": "448453867814780930", "category": "Weeb stuff" }, "deleted": false },
      { "id": "620303286548955166", "date": 1567900800000, "media": { "id": "734fe34b-481c-4ac5-9e3e-b4c973e0b6d1", "url": "http://174.138.0.194:34123/api/storage/get/734fe34b-481c-4ac5-9e3e-b4c973e0b6d1", "name": "unknown.png", "size": 1118317, "image": true }, "author": { "id": "292789928247689216", "name": "Killeras", "icon": "https://cdn.discordapp.com/avatars/292789928247689216/0e88751d9be61cbe7f75dc6491b7d43d.png" }, "channel": { "id": "454679745842642944", "name": "pogaduszki", "nsfw": false, "guild": "448453867814780930", "category": "Weeb stuff" }, "deleted": false }
    ];
  }

}
