package io.mosip.certify.service;

import io.mosip.certify.dto.ParsedAccessToken;

public interface TokenService {
    ParsedAccessToken getClaimsFromToken(String authorizationHeader) throws Exception;
}
