package com.shekhar.jobportal.controller;

import com.shekhar.jobportal.util.JwtUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@RestController
@RequestMapping("/api/oauth")
@CrossOrigin(origins = {"http://localhost:5173"})
public class OAuthController {

    @Value("${spring.security.oauth2.client.registration.google.client-id}")
    private String clientId;

    @Value("${spring.security.oauth2.client.registration.google.client-secret}")
    private String clientSecret;

    @Value("${spring.security.oauth2.client.registration.google.redirect-uri}")
    private String redirectUri;

    private final String TOKEN_URL = "https://oauth2.googleapis.com/token";
    private final String USERINFO_URL = "https://www.googleapis.com/oauth2/v3/userinfo";

    @GetMapping("/user")
    public Map<String, Object> getUserInfo(@AuthenticationPrincipal OAuth2User principal) {
        return principal.getAttributes();
    }

    public Map<String, Object> getUserInfo1(OAuth2AuthenticationToken authentication) {
        if (authentication == null) {
            throw new IllegalStateException("User is not authenticated");
        }

        OidcUser user = (OidcUser) authentication.getPrincipal();
        return user.getAttributes();
    }

    @GetMapping("/token")
    public String getAccessToken(OAuth2AuthenticationToken authentication) {
        OidcUser user = (OidcUser) authentication.getPrincipal();
        return user.getIdToken().getTokenValue(); //Access token from google
    }

    @PostMapping("/exchange-token")
    public ResponseEntity<Map> exchangeCodeForToken(@RequestBody Map<String, String> body) {
        String code = body.get("code");
        RestTemplate restTemplate = new RestTemplate();

        // Prepare request body
        MultiValueMap<String, String> form = new LinkedMultiValueMap<>();
        form.add("code", code);
        form.add("client_id", clientId);
        form.add("client_secret", clientSecret);
        form.add("redirect_uri", redirectUri);
        form.add("grant_type", "authorization_code");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(form, headers);

        try {
            ResponseEntity<Map> response = restTemplate.postForEntity(TOKEN_URL, request, Map.class);

            String accessToken = (String) response.getBody().get("access_token");
            HttpHeaders authHeaders = new HttpHeaders();
            authHeaders.setBearerAuth(accessToken);

            ResponseEntity<Map> userInfoResponse = restTemplate.exchange(
                    USERINFO_URL, HttpMethod.GET, new HttpEntity<>(authHeaders), Map.class
            );

            Map<String, Object> userInfo = userInfoResponse.getBody();

            String jwt = JwtUtil.generateToken((String) userInfo.get("email"));
            return ResponseEntity.ok(Map.of(
                    "email", userInfo.get("email"),
                    "name", userInfo.get("name"),
                    "role", "ROLE_APPLICANT",
                    "token", jwt
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/user-details")
    public Map<String, Object> getUserInfo(@RequestHeader("Authorization") String token) {
        String idToken = token.replace("Bearer ", "");

        //Verify the id token with google
        String userInfoEndpoint = "https://oauth2.googleapis.com/tokeninfo?id_token=" + idToken;
        RestTemplate restTemplate = new RestTemplate();

        ResponseEntity<Map<String, Object>> response = restTemplate.exchange(
                userInfoEndpoint,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<Map<String, Object>>() {
                }
        );

        Map<String, Object> body = response.getBody();
        return body;
    }

}