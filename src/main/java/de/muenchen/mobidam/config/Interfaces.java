package de.muenchen.mobidam.config;

import de.muenchen.mobidam.mobilithek.InterfaceDTO;
import java.util.Map;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "de.muenchen.mobidam.integration")
@Getter
@Setter
public class Interfaces {

    private Map<String, InterfaceDTO> interfaces;

}
