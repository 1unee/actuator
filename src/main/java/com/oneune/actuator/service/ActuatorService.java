package com.oneune.actuator.service;

import com.oneune.actuator.config.properties.ActuatorProperties;
import com.oneune.actuator.config.properties.ActuatorProperties.ServerName;
import com.oneune.actuator.config.properties.ActuatorProperties.ServerProperties;
import com.oneune.actuator.dto.ActuatorHealthDto;
import com.oneune.actuator.dto.ActuatorHealthDto.ServerStatus;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.log4j.Log4j2;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import java.net.URI;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Log4j2
public class ActuatorService {

    ActuatorProperties actuatorProperties;
    RestTemplate restTemplate;
    NotificationService notificationService;

    public boolean isServerAvailable(URI serverUrl) {
        boolean isServerAvailable;
        try {
            isServerAvailable = restTemplate
                    .getForObject(serverUrl, ActuatorHealthDto.class)
                    .getStatus()
                    .equals(ServerStatus.UP);
        } catch (ResourceAccessException e) {
            isServerAvailable = false;
        }
        return isServerAvailable;
    }

    private void monitorServer(ServerName serverName) {
        ServerProperties serverProperties = actuatorProperties.getServers().get(ServerName.MATER);
        boolean isServerAvailable = isServerAvailable(serverProperties.getUrl());
        if (isServerAvailable != serverProperties.isUp()) {
            serverProperties.setUp(isServerAvailable);
            String message = "%s server is %s.".formatted(serverName.name(), isServerAvailable ? "UP" : "DOWN");
            log.info(message);
            notificationService.sendSimpleMailToSelf("Actuator", message);
        }
    }

    @Scheduled(cron = "*/10 * * * * *")
    public void monitorServers() {
        actuatorProperties.getServers().keySet().forEach(this::monitorServer);
    }
}
