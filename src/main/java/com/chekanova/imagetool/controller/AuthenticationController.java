package com.chekanova.imagetool.controller;

import com.chekanova.imagetool.exception.BadRequestException;
import com.chekanova.imagetool.model.dto.AuthenticationRequest;
import com.chekanova.imagetool.security.JwtTokenProvider;
import com.chekanova.imagetool.security.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/auth")
public class AuthenticationController {
  private final PasswordEncoder passwordEncoder;
  private final UserService userService;
  private final JwtTokenProvider jwtTokenProvider;

  @PostMapping("/login")
  public ResponseEntity<Object> authenticate(@RequestBody AuthenticationRequest request) {
    var user = userService.get(request);
    if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
      throw new BadRequestException("Invalid email or password", HttpStatus.BAD_REQUEST);
    }
    return ResponseEntity.ok(
        Map.of(
            "email",
            request.getEmail(),
            "token",
            jwtTokenProvider.createToken(request.getEmail())));
  }
}
