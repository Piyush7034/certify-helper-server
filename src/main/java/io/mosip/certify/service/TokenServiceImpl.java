package io.mosip.certify.service;

import io.mosip.certify.dto.ParsedAccessToken;
import io.mosip.certify.util.CommonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.core.DelegatingOAuth2TokenValidator;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.time.Clock;
import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

@Service(value = "tokenService")
@Slf4j
public class TokenServiceImpl implements TokenService {
    @Value("${mosip.certify.authn.issuer-uri}")
    private String issuerUri;

    @Value("${mosip.certify.authn.jwk-set-uri}")
    private String jwkSetUri;

    @Value("#{${mosip.certify.authn.allowed-audiences}}")
    private List<String> allowedAudiences;

    @Autowired
    private ParsedAccessToken parsedAccessToken;

    private NimbusJwtDecoder nimbusJwtDecoder;

    private boolean isJwt(String token) {
        return token.split("\\.").length == 3;
    }

    private NimbusJwtDecoder getNimbusJwtDecoder() {
        if(nimbusJwtDecoder == null) {
            nimbusJwtDecoder = NimbusJwtDecoder.withJwkSetUri(jwkSetUri).build();
            nimbusJwtDecoder.setJwtValidator(new DelegatingOAuth2TokenValidator<>(
                    new JwtTimestampValidator(),
                    new JwtIssuerValidator(issuerUri),
                    new JwtClaimValidator<List<String>>(JwtClaimNames.AUD, allowedAudiences::containsAll),
                    new JwtClaimValidator<String>(JwtClaimNames.SUB, Objects::nonNull),
                    new JwtClaimValidator<String>("client_id", Objects::nonNull),
                    new JwtClaimValidator<Instant>(JwtClaimNames.IAT,
                            iat -> iat != null && iat.isBefore(Instant.now(Clock.systemUTC()))),
                    new JwtClaimValidator<Instant>(JwtClaimNames.EXP,
                            exp -> exp != null && exp.isAfter(Instant.now(Clock.systemUTC())))));
        }
        return nimbusJwtDecoder;
    }

    @Override
    public ParsedAccessToken getClaimsFromToken(String authorizationHeader) throws Exception {
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String token = authorizationHeader.substring(7);
            //validate access token no matter if its JWT or Opaque
            if (isJwt(token)) {
                try {
                    //Verifies signature and claim predicates, If invalid throws exception
                    Jwt jwt = getNimbusJwtDecoder().decode(token);
                    parsedAccessToken.setClaims(new HashMap<>());
                    parsedAccessToken.getClaims().putAll(jwt.getClaims());
                    parsedAccessToken.setAccessTokenHash(CommonUtil.generateOIDCAtHash(token));
                    parsedAccessToken.setActive(true);
                    return parsedAccessToken;
                } catch (Exception e) {
                    log.error("Access token validation failed", e);
                }
            }
        }
        throw new Exception("AUTH_FAILED");
    }
}
