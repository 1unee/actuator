package com.oneune.actuator.config.properties;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.util.Map;

@Component
@ConfigurationProperties(prefix = "actuator")
@PropertySource("classpath:application.yml")
@FieldDefaults(level = AccessLevel.PRIVATE)
@Data
public class ActuatorProperties {

    Map<ServerName, ServerProperties> servers;

    @FieldDefaults(level = AccessLevel.PRIVATE)
    @Data
    public static class ServerProperties {
        URI url;
        boolean up = false;
    }

    public enum ServerName {
        MATER
    }
}
