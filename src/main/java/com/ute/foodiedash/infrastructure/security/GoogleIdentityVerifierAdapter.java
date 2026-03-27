package com.ute.foodiedash.infrastructure.security;

import com.ute.foodiedash.application.user.port.GoogleIdentityVerifierPort;
import com.ute.foodiedash.application.user.query.GoogleIdentityQueryResult;
import com.ute.foodiedash.domain.common.exception.BadRequestException;
import com.ute.foodiedash.domain.common.exception.UnauthorizedException;
import com.ute.foodiedash.infrastructure.config.GoogleOAuthProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.time.Duration;
import java.time.Instant;
import java.util.Map;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class GoogleIdentityVerifierAdapter implements GoogleIdentityVerifierPort {

    private static final String TOKEN_INFO_URL = "https://oauth2.googleapis.com/tokeninfo";
    private static final Set<String> VALID_ISSUERS = Set.of(
            "accounts.google.com",
            "https://accounts.google.com"
    );

    private final GoogleOAuthProperties googleOAuthProperties;
    private final WebClient googleTokenInfoWebClient;

    @Override
    public GoogleIdentityQueryResult verifyIdToken(String idToken) {
        if (idToken == null || idToken.isBlank()) {
            throw new BadRequestException("Google id token is required");
        }
        if (googleOAuthProperties.getClientId() == null || googleOAuthProperties.getClientId().isBlank()) {
            throw new BadRequestException("Google client id is not configured");
        }

        Map<String, Object> payload = fetchTokenInfo(idToken);

        String aud = stringValue(payload.get("aud"));
        if (!googleOAuthProperties.getClientId().equals(aud)) {
            throw new UnauthorizedException("Google token audience mismatch");
        }

        String issuer = stringValue(payload.get("iss"));
        if (!VALID_ISSUERS.contains(issuer)) {
            throw new UnauthorizedException("Google token issuer is invalid");
        }

        String expValue = stringValue(payload.get("exp"));
        long expEpochSeconds;
        try {
            expEpochSeconds = Long.parseLong(expValue);
        } catch (NumberFormatException e) {
            throw new UnauthorizedException("Google token expiry is invalid");
        }
        if (Instant.ofEpochSecond(expEpochSeconds).isBefore(Instant.now())) {
            throw new UnauthorizedException("Google token is expired");
        }

        String email = stringValue(payload.get("email"));
        String name = stringValue(payload.get("name"));
        String picture = stringValue(payload.get("picture"));
        String sub = stringValue(payload.get("sub"));
        boolean emailVerified = Boolean.parseBoolean(stringValue(payload.get("email_verified")));

        if (email.isBlank()) {
            throw new UnauthorizedException("Google token does not contain email");
        }

        return new GoogleIdentityQueryResult(sub, email, name, picture, emailVerified);
    }

    private Map<String, Object> fetchTokenInfo(String idToken) {
        try {
            return googleTokenInfoWebClient
                    .get()
                    .uri(TOKEN_INFO_URL + "?id_token={idToken}", idToken)
                    .retrieve()
                    .onStatus(HttpStatusCode::isError, response ->
                            response.createException().map(ex ->
                                    new UnauthorizedException("Google token verification failed", ex)
                            )
                    )
                    .bodyToMono(Map.class)
                    .timeout(Duration.ofSeconds(5))
                    .block();
        } catch (WebClientResponseException e) {
            throw new UnauthorizedException("Google token verification failed", e);
        } catch (Exception e) {
            throw new UnauthorizedException("Unable to verify Google token", e);
        }
    }

    private String stringValue(Object value) {
        return value == null ? "" : value.toString().trim();
    }
}

