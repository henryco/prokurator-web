package dev.tindersamurai.prokurator.mvc.api;

import dev.tindersamurai.prokurator.mvc.service.auth.AuthenticationTokenExtractor;
import dev.tindersamurai.prokurator.mvc.service.guild.GuildDataService;
import dev.tindersamurai.prokurator.mvc.service.user.UserDataService;
import dev.tindersamurai.prokurator.mvc.service.user.UserDataService.TokenExpiredException;
import io.swagger.annotations.Api;
import javassist.NotFoundException;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RestController @Api @Slf4j
@RequestMapping("/api/protected/guild")
public class GuildController {

    private final AuthenticationTokenExtractor extractor;
    private final GuildDataService guildDataService;
    private final UserDataService userDataService;

    @Autowired
    public GuildController(
            AuthenticationTokenExtractor extractor,
            GuildDataService guildDataService,
            UserDataService userDataService
    ) {
        this.extractor = extractor;
        this.guildDataService = guildDataService;
        this.userDataService = userDataService;
    }


    @Value @Builder @AllArgsConstructor
    private static class GuildDetailsForm {
        private String id;
        private String name;
        private String icon;
        private Boolean admin;
    }

    @Value @Builder @AllArgsConstructor
    private static class DetailsForm {
        private String id;
        private String name;
    }

    @GetMapping("/{id}")
    public GuildDetailsForm getDetails(
            @PathVariable("id") String id,
            Authentication authentication
    ) throws TokenExpiredException, NotFoundException {
        log.debug("getDetails: {}, {}", id, authentication);

        val token = extractor.extractToken(authentication);
        val fetched = userDataService.retrieveUserGuilds(token.getAccess());

        val first = fetched.stream().filter(f -> f.getId().equals(id)).findFirst();
        if (!first.isPresent()) {
            throw new NotFoundException("Guild: " + id + "does not exists");
        }

        val cacheId = token.getTokenId() + "-" + id;
        val guild = first.get();

        val guilds = guildDataService.filterHandledGuilds(new String[] {guild.getId()}, cacheId);
        if (!Arrays.asList(guilds).contains(id)) {
            throw new NotFoundException("Guild: " + id + " are not handled by bot [yet...]");
        }

        return GuildDetailsForm.builder()
                .admin(isOwner(guild.getPermissions()))
                .name(guild.getName())
                .icon(guild.getIcon())
                .build();
    }

    @GetMapping("/{id}/users")
    public List<DetailsForm> fetchUsers(
            @RequestParam(value = "query", required = false) String query,
            @PathVariable("id") String id
    ) {
        log.debug("fetchUsers: {}, {}", id, query);
        final Stream<DetailsForm> stream = Arrays.stream(guildDataService.fetchGuildMembers(id))
                .map(e -> new DetailsForm(e.getId(), e.getName()));
        if (query == null || query.isEmpty())
            return stream.limit(100).collect(Collectors.toList());
        return stream.filter(e -> e.getName().contains(query))
                .limit(100).collect(Collectors.toList());
    }

    @GetMapping("/{id}/channels")
    public List<DetailsForm> fetchChannels(
            @RequestParam(value = "query", required = false) String query,
            @PathVariable("id") String id
    ) {
        log.debug("fetchChannels: {}, {}", id, query);

        final Stream<DetailsForm> stream = Arrays.stream(guildDataService.fetchGuildChannels(id))
                .map(e -> new DetailsForm(e.getId(), e.getName()));
        if (query == null || query.isEmpty())
            return stream.collect(Collectors.toList());

        return stream.filter(e -> e.getName().contains(query))
                .collect(Collectors.toList());
    }

    private static boolean isOwner(String permissions) {
        val p = Long.decode(permissions);
        log.debug("permissions: {}" ,permissions);
        return (p & 0x8) == 0x8 || (p & 0x20) == 0x20;
    }
}
