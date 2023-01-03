package com.chekanova.imagetool.model.dto;

import lombok.Data;

@Data
public class AuthenticationRequest {
    private String email;
    private String password;
}