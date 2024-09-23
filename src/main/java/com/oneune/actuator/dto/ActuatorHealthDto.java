package com.oneune.actuator.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Data
public class ActuatorHealthDto {

    ServerStatus status;

    @FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
    @RequiredArgsConstructor
    @Getter
    public enum ServerStatus {

        UP("Сервис работает корректно и доступен"),
        DOWN("Сервис или его компоненты не работают корректно или недоступны"),
        OUT_OF_SERVICE("Компонент временно недоступен"),
        UNKNOWN("Состояние компонента неизвестно");

        String description;
    }
}
