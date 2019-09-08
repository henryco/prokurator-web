package dev.tindersamurai.prokurator.mvc.service.auth;

import dev.tindersamurai.prokurator.configuration.security.auth.credentials.DiscordTokenPrincipal;
import dev.tindersamurai.prokurator.configuration.security.auth.session.service.access.TokenAccessService;
import dev.tindersamurai.prokurator.configuration.security.auth.session.service.access.TokenAccessService.Token;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service @Slf4j
public class BotAuthenticationTokenExtractor implements AuthenticationTokenExtractor {

    private final TokenAccessService tokenAccessService;

    @Autowired
    public BotAuthenticationTokenExtractor(TokenAccessService tokenAccessService) {
        this.tokenAccessService = tokenAccessService;
    }

    @Override
    public Token extractToken(Authentication authentication) throws UnsupportedPrincipalException {
        log.debug("extractToken: {}", authentication);
        if (authentication.getPrincipal() == null)
            throw new NullPointerException("Principal cannot be null");
        try {
            val principal = ((DiscordTokenPrincipal) authentication.getPrincipal());
            return tokenAccessService.getToken(principal.getTokenId());
        } catch (ClassCastException e) {
            throw new UnsupportedPrincipalException(authentication.getPrincipal().getClass());
        }
    }
}
