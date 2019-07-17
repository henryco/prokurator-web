package dev.tindersamurai.prokurator.configuration.security.auth.session.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import java.util.Date;

import static javax.persistence.TemporalType.TIMESTAMP;

@Entity @Data
@NoArgsConstructor
@AllArgsConstructor
public class DiscordTokenEntity {

	private @Id @Column(
			name = "token_id"
	) String id;

	private @Column(
			name = "user_id",
			nullable = false,
			updatable = false
	) String userId;

	private @Column(
			name = "extra_data",
			updatable = false,
			length = 512
	) String extraData;

	private @Column(
			name = "created",
			nullable = false,
			updatable = false
	) @Temporal(
			TIMESTAMP
	) Date created;

	private @Column(
			name = "expired",
			nullable = false
	) @Temporal(
			TIMESTAMP
	) Date expired;

	private @Column(
			name = "refresh",
			nullable = false
	) String refreshToken;

	private @Column(
			name = "token",
			nullable = false
	) String accessToken;
}
