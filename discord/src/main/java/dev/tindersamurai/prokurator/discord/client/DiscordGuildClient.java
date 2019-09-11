package dev.tindersamurai.prokurator.discord.client;

import dev.tindersamurai.prokurator.discord.DiscordGuildRepository;

import java.util.List;

public interface DiscordGuildClient {
    List<DiscordGuildRepository.GuildChannel> getGuildChannels(String botToken, String guildId);

    List<DiscordGuildRepository.GuildMember> getGuildMembers(String botToken, String guildId);
}
