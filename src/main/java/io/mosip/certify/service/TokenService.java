package io.mosip.certify.service;

public interface TokenService {
    String getClaimsFromToken(String authorizationHeader) throws Exception;
}
