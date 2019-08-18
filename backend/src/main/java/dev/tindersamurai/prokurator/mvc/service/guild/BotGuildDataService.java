package dev.tindersamurai.prokurator.mvc.service.guild;

import dev.tindersamurai.prokurator.backend.commons.service.IGuildService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service @Slf4j
public class BotGuildDataService implements GuildDataService {

    private final IGuildService service;

    @Autowired
    public BotGuildDataService(IGuildService service) {
        this.service = service;
    }

    @Override @Cacheable(value="guilds", key = "#cacheId")
    public String[] filterHandledGuilds(String[] guilds, String cacheId) {
        log.debug("filterHandledGuilds: {}", cacheId);
        return service.filterHandledGuilds(guilds);
    }
}
