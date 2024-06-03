package de.muenchen.mobidam.mobilithek;

import org.apache.camel.CamelContext;
import org.apache.camel.component.http.HttpComponent;
import org.apache.camel.support.jsse.KeyManagersParameters;
import org.apache.camel.support.jsse.KeyStoreParameters;
import org.apache.camel.support.jsse.SSLContextParameters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Profile("!test")
@Configuration
public class MobilithekSSL {

    @Value("${mobidam.mobilithek.jks-password}")
    private String keyPass;

    @Autowired
    private CamelContext context;

    @Bean
    public HttpComponent httpComponent() {

        KeyStoreParameters ksp = new KeyStoreParameters();
        ksp.setResource("classpath:mobilithek.jks");
        ksp.setPassword(keyPass);

        KeyManagersParameters kmp = new KeyManagersParameters();
        kmp.setKeyStore(ksp);
        kmp.setKeyPassword(keyPass);

        SSLContextParameters scp = new SSLContextParameters();
        scp.setKeyManagers(kmp);

        HttpComponent httpComponent = context.getComponent("https", HttpComponent.class);
        httpComponent.setSslContextParameters(scp);

        return httpComponent;

    }

}
