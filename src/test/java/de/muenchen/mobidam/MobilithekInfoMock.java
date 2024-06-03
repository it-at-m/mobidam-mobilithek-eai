package de.muenchen.mobidam;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;

import java.io.FileInputStream;

public class MobilithekInfoMock implements Processor {
    @Override
    public void process(Exchange exchange) throws Exception {
        exchange.getIn().setBody(new FileInputStream("src/test/resources/mobilithek-info-source-pr-daten.xml"));
    }
}
