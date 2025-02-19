package io.mosip.certify.controller;

import io.mosip.certify.dto.ParsedAccessToken;
import io.mosip.certify.service.TokenService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/token")
public class TokenController {
    @Autowired
    private TokenService tokenService;

    @PostMapping("/validate")
    public ResponseEntity<ParsedAccessToken> validateToken(@RequestHeader("Authorization") String authHeader) {
        try {
            ParsedAccessToken parsedAccessToken = tokenService.getClaimsFromToken(authHeader);

            return ResponseEntity.ok(parsedAccessToken);
        } catch (Exception e) {
            log.error("Authorization Failed", e);
        }

        throw new RuntimeException("AUTH_FAILED");
    }

}
