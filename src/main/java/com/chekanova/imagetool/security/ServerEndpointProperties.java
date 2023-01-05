package com.chekanova.imagetool.security;

import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Data
@Accessors(chain = true)
@Configuration
@ConfigurationProperties("server.endpoints")
public class ServerEndpointProperties {
    private List<String> publiclyAvailable;
}
