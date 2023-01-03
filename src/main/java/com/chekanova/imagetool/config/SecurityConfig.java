package com.chekanova.imagetool.config;

import com.chekanova.imagetool.security.JwtTokenFilter;
import com.chekanova.imagetool.security.ServerEndpointProperties;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@AllArgsConstructor
@EnableGlobalMethodSecurity(jsr250Enabled = true)
public class SecurityConfig {
  private static final int ENCODER_STRENGTH = 12;
  private final JwtTokenFilter filter;
  private final AuthenticationConfiguration configuration;
  private final ServerEndpointProperties endpointProperties;

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity security) throws Exception {
    security
        .cors()
        .and()
        .csrf()
        .disable()
        .sessionManagement()
        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        .and()
        .exceptionHandling()
        .authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED))
        .and()
        .authorizeRequests()
        .antMatchers(endpointProperties.getPubliclyAvailable().toArray(new String[] {}))
        .permitAll()
        .anyRequest()
        .authenticated()
        .and()
        .addFilterBefore(filter, UsernamePasswordAuthenticationFilter.class);
    return security.build();
  }

  @Bean
  public AuthenticationManager authenticationManager() throws Exception {
    return configuration.getAuthenticationManager();
  }

  @Bean
  public BCryptPasswordEncoder bCryptPasswordEncoder() {
    return new BCryptPasswordEncoder(ENCODER_STRENGTH);
  }
}
