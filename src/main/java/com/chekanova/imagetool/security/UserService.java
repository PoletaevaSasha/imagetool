package com.chekanova.imagetool.security;

import com.chekanova.imagetool.exception.BadRequestException;
import com.chekanova.imagetool.model.User;
import com.chekanova.imagetool.model.dto.AuthenticatedUser;
import com.chekanova.imagetool.model.dto.AuthenticationRequest;
import com.chekanova.imagetool.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class UserService {
  private final UserRepository userRepository;

  public User get(AuthenticationRequest request) {
    return userRepository
        .findByEmail(request.getEmail())
        .orElseThrow(
            () -> new BadRequestException("Invalid email or password", HttpStatus.BAD_REQUEST));
  }

  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    var user =
        userRepository
            .findByEmail(username)
            .orElseThrow(() -> new UsernameNotFoundException("User doesn't exist"));
    return new AuthenticatedUser().setEmail(user.getEmail()).setRoles(List.of(user.getRole()));
  }
}
