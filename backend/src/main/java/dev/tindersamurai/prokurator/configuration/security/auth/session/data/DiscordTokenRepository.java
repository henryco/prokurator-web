package dev.tindersamurai.prokurator.configuration.security.auth.session.data;

import org.springframework.data.jpa.repository.JpaRepository;

public interface DiscordTokenRepository extends JpaRepository<DiscordTokenEntity, String> {

	void deleteAllByUserId(String username);
}
