package com.chekanova.imagetool.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.sql.Timestamp;
import java.time.LocalDateTime;

@RequiredArgsConstructor
@Component
public class JwtTokenProvider {
  private static final String AUTHORIZATION_HEADER = "Authorization";
  public static final String EMAIL_CLAIM = "email";
  private final UserService userService;
  private final AccessTokenProperties tokenProperties;

  public String createToken(String email) {
    var now = LocalDateTime.now();
    var expires = now.plus(tokenProperties.getTokenExpiration());
    return JWT.create()
        .withClaim(EMAIL_CLAIM, email)
        .withIssuedAt(Timestamp.valueOf(now))
        .withExpiresAt(Timestamp.valueOf(expires))
        .withIssuer(tokenProperties.getIssuer())
        .sign(Algorithm.HMAC256(tokenProperties.getSecret()));
  }

  public String verifyAccessToken(String token) {
    var verifier =
        JWT.require(Algorithm.HMAC256(tokenProperties.getSecret()))
            .withIssuer(tokenProperties.getIssuer())
            .build();
    try {
      var jwt = verifier.verify(token);
      return jwt.getClaim(EMAIL_CLAIM).asString();
    } catch (JWTVerificationException e) {
      throw new BadCredentialsException("Invalid token");
    }
  }

  public Authentication getAuthentication(String email) {
    var userDetails = userService.loadUserByUsername(email);
    return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
  }

  public String resolveToken(HttpServletRequest request) {
    return request.getHeader(AUTHORIZATION_HEADER);
  }
}
