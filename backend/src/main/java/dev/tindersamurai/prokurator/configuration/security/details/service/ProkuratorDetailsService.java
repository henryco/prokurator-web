package dev.tindersamurai.prokurator.configuration.security.details.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service @Slf4j
public class ProkuratorDetailsService implements IProkuratorDetailsService {

	@Override
	public UserDetails loadByDiscordTokenCode(@NonNull String discordTokenCode) throws UsernameNotFoundException {
		log.info("Discord token code: {}", discordTokenCode);
		if (discordTokenCode.isEmpty())
			throw new UsernameNotFoundException("Discord token code should be provided");
		return null;
	}

}
