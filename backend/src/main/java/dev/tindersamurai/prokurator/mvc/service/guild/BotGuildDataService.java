package dev.tindersamurai.prokurator.mvc.service.guild;

import dev.tindersamurai.prokurator.backend.commons.service.IGuildService;
import dev.tindersamurai.prokurator.configuration.props.secrets.ProkuratorSecrets;
import dev.tindersamurai.prokurator.discord.DiscordGuildRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service @Slf4j
public class BotGuildDataService implements GuildDataService {

    private final DiscordGuildRepository guildRepository;
    private final ProkuratorSecrets secrets;
    private final IGuildService service;

    @Autowired
    public BotGuildDataService(
            DiscordGuildRepository guildRepository,
            ProkuratorSecrets secrets,
            IGuildService service
    ) {
        this.guildRepository = guildRepository;
        this.secrets = secrets;
        this.service = service;
    }

    @Override @Cacheable(value="handled", key = "#cacheId")
    public String[] filterHandledGuilds(String[] guilds, String cacheId) {
        log.debug("filterHandledGuilds: {}", cacheId);
        return service.filterHandledGuilds(guilds);
    }

    @Override @Cacheable(value="members", key = "#guildId")
    public Details[] fetchGuildMembers(String guildId) {
        log.debug("fetchGuildMembers: {}", guildId);
        return guildRepository.getGuildMembers(secrets.getBotToken(), guildId).stream()
                .map(l -> new Details(l.getUser().getId(), l.getUser().getUsername()))
                .toArray(Details[]::new);
    }

    @Override @Cacheable(value="channels", key = "#guildId")
    public Details[] fetchGuildChannels(String guildId) {
        log.debug("fetchGuildChannels: {}", guildId);
        return new Details[0];
    }
}
