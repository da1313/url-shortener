package org.example.configuration;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@ConfigurationProperties(prefix = "shortener")
@Configuration
@Getter
@Setter
@NoArgsConstructor
public class ApplicationProperties {

    private String host;
    private int maxCollisionCount;
    private int timeout;
    private boolean directValidation;

}
