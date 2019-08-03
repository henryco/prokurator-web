package dev.tindersamurai.prokurator.mvc.service.user;

import dev.tindersamurai.prokurator.discord.DiscordUserInfoRepository;
import dev.tindersamurai.prokurator.discord.DiscordUserInfoRepository.GuildsResponse;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service @Slf4j
public class DiscordUserDataService implements UserDataService {

	private final DiscordUserInfoRepository userInfoRepository;

	@Autowired
	public DiscordUserDataService(DiscordUserInfoRepository userInfoRepository) {
		this.userInfoRepository = userInfoRepository;
	}

	@Override @Cacheable(value="self", key = "#userAccessToken")
	public UserData retrieveUserData(String userAccessToken) throws TokenExpiredException {
		log.debug("retrieveUserData: {}", userAccessToken);
		try {
			val r = userInfoRepository.getUserInfo(userAccessToken);
			return new UserData(r.getId(), r.getUsername(), r.getDiscriminator(), r.getAvatar());
		} catch (Exception e) {
			throw new TokenExpiredException(userAccessToken);
		}
	}

	@Override @Cacheable(value="user", key = "#userId")
	public UserData retrieveUserData(String userAccessToken, String userId) throws TokenExpiredException {
		log.debug("retrieveUserData: {}, {}", userAccessToken, userId);
		try {
			val r = userInfoRepository.getUserInfo(userAccessToken, userId);
			return new UserData(r.getId(), r.getUsername(), r.getDiscriminator(), r.getAvatar());
		} catch (Exception e) {
			throw new TokenExpiredException(userAccessToken);
		}
	}

	@Override @Cacheable(value="guilds", key = "#userAccessToken")
	public List<Guild> retrieveUserGuilds(String userAccessToken) throws TokenExpiredException {
		log.debug("retrieveUserGuilds: {}", userAccessToken);
		try {
			return userInfoRepository.getUserGuilds(userAccessToken).stream()
					.map(DiscordUserDataService::mapGuild)
					.collect(Collectors.toList());
		} catch (Exception e) {
			throw new TokenExpiredException(userAccessToken);
		}
	}

	private static Guild mapGuild(GuildsResponse g) {
		return new Guild(g.getId(), g.getName(), g.getIcon(), g.getOwner(), g.getPermissions());
	}
}
