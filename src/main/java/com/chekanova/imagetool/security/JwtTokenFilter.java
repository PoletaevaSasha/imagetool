package com.chekanova.imagetool.security;

import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@AllArgsConstructor
public class JwtTokenFilter extends OncePerRequestFilter {
  private final JwtTokenProvider jwtTokenProvider;

  @Override
  public void doFilterInternal(
      HttpServletRequest request, HttpServletResponse response, FilterChain chain)
      throws IOException, ServletException {
    var token = jwtTokenProvider.resolveToken(request);
    if (token != null) {
      var email = jwtTokenProvider.verifyAccessToken(token);
      Authentication authentication = jwtTokenProvider.getAuthentication(email);
      setAuthentication(authentication);
    }
    chain.doFilter(request, response);
  }

  private void setAuthentication(Authentication authentication) {
    if (authentication != null) {
      SecurityContextHolder.getContext().setAuthentication(authentication);
    }
  }
}
