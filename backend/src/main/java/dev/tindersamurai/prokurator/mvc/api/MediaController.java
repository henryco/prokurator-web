package dev.tindersamurai.prokurator.mvc.api;

import dev.tindersamurai.prokurator.backend.commons.entity.MediaContent;
import dev.tindersamurai.prokurator.backend.commons.entity.MediaProbe;
import dev.tindersamurai.prokurator.backend.commons.service.IFileStorageService;
import dev.tindersamurai.prokurator.backend.commons.service.IMediaService;
import dev.tindersamurai.prokurator.mvc.service.auth.AuthenticationTokenExtractor;
import dev.tindersamurai.prokurator.mvc.service.guild.GuildDataService;
import dev.tindersamurai.prokurator.mvc.service.user.UserDataService;
import dev.tindersamurai.prokurator.mvc.service.user.UserDataService.TokenExpiredException;
import io.swagger.annotations.Api;
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
import java.util.Map;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import static dev.tindersamurai.prokurator.mvc.service.guild.GuildDataService.*;

@RestController @Slf4j @Api
@RequestMapping("/api/protected/media")
public class MediaController {

    @Value @Builder @AllArgsConstructor
    private static class Content {
        private String id;
        private long date;
        private Media media;
        private Author author;
        private Channel channel;
        private boolean deleted;
    }

    @Value @Builder @AllArgsConstructor
    private static class Media {
        private String id;
        private String url;
        private String name;
        private long size;
        private boolean image;
    }

    @Value @Builder @AllArgsConstructor
    private static class Channel {
        private String id;
        private String name;
        private boolean nsfw;
        private String guild;
        private String category;
    }

    @Value @Builder @AllArgsConstructor
    private static class Author {
        private String id;
        private String name;
        private String icon;
    }

    private final AuthenticationTokenExtractor extractor;
    private final IFileStorageService storageService;
    private final GuildDataService guildDataService;
    private final UserDataService userDataService;
    private final IMediaService mediaService;

    @Autowired
    public MediaController(
            AuthenticationTokenExtractor extractor,
            IFileStorageService storageService,
            GuildDataService guildDataService,
            UserDataService userDataService,
            IMediaService mediaService
    ) {
        this.storageService = storageService;
        this.guildDataService = guildDataService;
        this.userDataService = userDataService;
        this.mediaService = mediaService;
        this.extractor = extractor;
    }

    private static boolean isOwner(String permissions) {
        val p = Long.decode(permissions);
        return (p & 0x8) == 0x8 || (p & 0x20) == 0x20;
    }

    private static Map<String, Details> toMap(Details[] details) {
        return Arrays.stream(details).collect(Collectors.toMap(
                Details::getId, d -> d
        ));
    }

    private static String safe(Supplier<String> supplier, String defValue) {
        try {
            val s = supplier.get();
            return s == null ? defValue : s;
        } catch (Exception e) {
            return defValue;
        }
    }

    private static String userIcon(Details details) {
        if (details == null) return null;
        return "https://cdn.discordapp.com/avatars/" + details.getId() + "/" + details.getIcon() + ".png";
    }

    private Content mapContent(MediaContent c) {
        val guild = c.getGuild();

        val channels = toMap(guildDataService.fetchGuildChannels(guild));
        val channel = Channel.builder()
                .name(safe(() -> channels.get(c.getChannel()).getName(), c.getChannel()))
                .category(c.getCategory())
                .id(c.getChannel())
                .nsfw(c.isNsfw())
                .guild(guild)
                .build();

        val members = toMap(guildDataService.fetchGuildMembers(guild));

        val author = Author.builder()
                .name(safe(() -> members.get(c.getAuthor()).getName(), c.getAuthor()))
                .icon(userIcon(members.get(c.getAuthor())))
                .id(c.getAuthor())
                .build();

        val media = Media.builder()
                .url(storageService.getFileUrl(c.getFile()))
                .id(c.getFile())
                .name(c.getName())
                .image(c.isImage())
                .size(c.getSize())
                .build();

        return Content.builder()
                .deleted(c.isRemoved())
                .date(c.getDate())
                .id(c.getId())
                .channel(channel)
                .author(author)
                .media(media)
                .build();
    }

    @PostMapping("/{guild_id}")
    public List<Content> getMediaContent(
            @PathVariable("guild_id") String guildId,
            @RequestBody MediaProbe probe,
            Authentication authentication
    ) throws TokenExpiredException, IllegalAccessException {
        log.debug("getMediaContent: {}, {}, {}", guildId, probe, authentication);

        val token = extractor.extractToken(authentication);
        val guilds = userDataService.retrieveUserGuilds(token.getAccess());

        val guild = guilds.stream().filter(g -> g.getId().equals(guildId)).findFirst();
        if (!guild.isPresent()) {
            throw new IllegalAccessException("Permission denied, user dont belong to this guild.");
        }

        val query = probe.getQuery();
        if (query != null && query.getDeleted() != null && query.getDeleted().length != 0) {
            // So, we check it coz only server owners can see deleted messages (generally)
            if (!isOwner(guild.get().getPermissions())) {
                throw new IllegalAccessException("User dont have permissions to read [deleted] messages");
            }
        }

        return mediaService.filterMedia(probe, guildId).stream()
                .map(this::mapContent).collect(Collectors.toList());
    }
}
