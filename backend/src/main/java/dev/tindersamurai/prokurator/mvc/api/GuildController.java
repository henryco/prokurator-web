package dev.tindersamurai.prokurator.mvc.api;

import dev.tindersamurai.prokurator.configuration.security.auth.credentials.DiscordTokenPrincipal;
import dev.tindersamurai.prokurator.configuration.security.auth.session.service.access.TokenAccessService;
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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;

@RestController @Api @Slf4j
@RequestMapping("/api/protected/guild")
public class GuildController {

    private final TokenAccessService tokenAccessService;
    private final GuildDataService guildDataService;
    private final UserDataService userDataService;

    @Autowired
    public GuildController(
            TokenAccessService tokenAccessService,
            GuildDataService guildDataService,
            UserDataService userDataService
    ) {
        this.tokenAccessService = tokenAccessService;
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

    @GetMapping("/{id}")
    public GuildDetailsForm getDetails(
            @PathVariable("id") String id,
            Authentication authentication
    ) throws TokenExpiredException, NotFoundException {
        log.debug("getDetails: {}", authentication);

        val principal = ((DiscordTokenPrincipal) authentication.getPrincipal());
        val token = tokenAccessService.getToken(principal.getTokenId());
        val fetched = userDataService.retrieveUserGuilds(token.getAccess());

        val first = fetched.stream().filter(f -> f.getId().equals(id)).findFirst();
        if (!first.isPresent()) {
            throw new NotFoundException("Guild: " + id + "does not exists");
        }

        val cacheId = principal.getTokenId() + "-" + id;
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

    private static boolean isOwner(String permissions) {
        val p = Long.decode(permissions);
        return (p & 0x8) == 0x8 || (p & 0x20) == 0x20;
    }
}
