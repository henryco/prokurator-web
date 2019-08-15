package dev.tindersamurai.prokurator.mvc.api;

import dev.tindersamurai.prokurator.configuration.security.auth.credentials.DiscordTokenPrincipal;
import dev.tindersamurai.prokurator.configuration.security.auth.session.service.access.TokenAccessService;
import dev.tindersamurai.prokurator.mvc.service.guild.GuildDataService;
import dev.tindersamurai.prokurator.mvc.service.user.UserDataService;
import dev.tindersamurai.prokurator.mvc.service.user.UserDataService.Guild;
import dev.tindersamurai.prokurator.mvc.service.user.UserDataService.TokenExpiredException;
import io.swagger.annotations.Api;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController @Slf4j @Api
@RequestMapping("/api/protected/identity")
public class IdentityController {

    private final TokenAccessService tokenAccessService;
    private final GuildDataService guildDataService;
    private final UserDataService userDataService;

    @Autowired
    public IdentityController(
            TokenAccessService tokenAccessService,
            GuildDataService guildDataService,
            UserDataService userDataService
    ) {
        this.tokenAccessService = tokenAccessService;
        this.guildDataService = guildDataService;
        this.userDataService = userDataService;
    }

    @Value private static class UserForm {
        private String name;
        private String icon;
        private String id;
    }

    @Value private static class UserGuild {
        private String name;
        private String icon;
        private String id;
        private boolean installed;
    }

    @GetMapping("/user")
    public UserForm getSelfInformation(Authentication authentication) throws TokenExpiredException {
        val principal = ((DiscordTokenPrincipal) authentication.getPrincipal());
        val token = tokenAccessService.getToken(principal.getTokenId());
        val user = userDataService.retrieveUserData(token.getAccess());
        return new UserForm(user.getUsername(), user.getAvatar(), user.getId());
    }

    @GetMapping("/guilds")
    public UserGuild[] getSelfGuildInformation(Authentication authentication) throws TokenExpiredException {
        val principal = ((DiscordTokenPrincipal) authentication.getPrincipal());
        val token = tokenAccessService.getToken(principal.getTokenId());
        val guilds = userDataService.retrieveUserGuilds(token.getAccess());

        log.debug("user guilds: {}", guilds);
        val ids = guilds.stream().map(Guild::getId).toArray(String[]::new);
        val handled = guildDataService.filterHandledGuilds(ids, principal.getTokenId());

        return guilds.stream().filter(e -> isOwner(e.getPermissions())).map(
                e -> new UserGuild(e.getName(), e.getIcon(), e.getId(), contains(handled, e.getId()))
        ).toArray(UserGuild[]::new);
    }

    private static boolean isOwner(String permissions) {
        val p = Long.decode(permissions);
        return (p & 0x8) == 0x8 || (p & 0x20) == 0x20;
    }

    private static boolean contains(String[] arr, String obj) {
        for (val s : arr)
            if (s.equals(obj))
                return true;
        return false;
    }
}
