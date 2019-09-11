package dev.tindersamurai.prokurator.mvc.service.auth;

import dev.tindersamurai.prokurator.configuration.security.auth.credentials.DiscordTokenPrincipal;
import org.springframework.security.core.Authentication;

import static dev.tindersamurai.prokurator.configuration.security.auth.session.service.access.TokenAccessService.*;

public interface AuthenticationTokenExtractor {

    final class UnsupportedPrincipalException extends RuntimeException {
        public UnsupportedPrincipalException(Class<?> c) {
            super("Unsupported principal type, should: "
                    + DiscordTokenPrincipal.class.getSimpleName()
                    + " got: " + c.getSimpleName()
            );
        }
    }

    Token extractToken(Authentication authentication) throws UnsupportedPrincipalException;
}
