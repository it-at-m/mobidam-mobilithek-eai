package de.muenchen.mobidam.security;

import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class CodeDetectorFactory {

    private final MaliciousXmlCodeDetector maliciousXmlCodeDetector;
    private final DefaultMaliciousCodeDetector defaultMaliciousCodeDetector;

    private final Map<String, MaliciousCodeDetector> map = new HashMap<>();

    @PostConstruct
    public void init() {
        map.put(MediaType.APPLICATION_XML_VALUE, maliciousXmlCodeDetector);
    }

    public MaliciousCodeDetector getCodeDetector(final String mimeType) {
        return map.getOrDefault(mimeType, defaultMaliciousCodeDetector);
    }

}
