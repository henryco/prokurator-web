package dev.tindersamurai.prokurator.mvc.service.guild;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

public interface GuildDataService {

    @Value @Builder @AllArgsConstructor class Details {
        private String id;
        private String name;
        private String icon;
    }

    String[] filterHandledGuilds(String[] guilds, String cacheId);

    Details[] fetchGuildMembers(String guildId);

    Details[] fetchGuildChannels(String guildId);
}
