package dev.tindersamurai.prokurator.configuration.security.details.service;

import org.springframework.lang.NonNull;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public interface IProkuratorDetailsService {

	UserDetails loadByDiscordTokenCode(@NonNull String discordTokenCode) throws UsernameNotFoundException;
}
