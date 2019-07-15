package dev.tindersamurai.prokurator.configuration.security.auth.session.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity @Data
@NoArgsConstructor
@AllArgsConstructor
public class DiscordTokenEntity {

	private @Id @Column(
			name = "token_id"
	) String id;

	private @Column(
			name = "username",
			nullable = false,
			updatable = false
	) String username;

	private @Column(
			name = "extra_data",
			updatable = false,
			length = 512
	) String extraData;

}
