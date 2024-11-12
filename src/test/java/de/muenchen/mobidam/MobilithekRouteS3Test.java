/*
 * The MIT License
 * Copyright © 2024 Landeshauptstadt München | it@M
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package de.muenchen.mobidam;

import de.muenchen.mobidam.config.Interfaces;
import de.muenchen.mobidam.eai.common.CommonConstants;
import de.muenchen.mobidam.eai.common.exception.MobidamException;
import de.muenchen.mobidam.eai.common.s3.S3CredentialProvider;
import de.muenchen.mobidam.exception.MobidamSecurityException;
import de.muenchen.mobidam.integration.client.domain.DatentransferCreateDTO;
import de.muenchen.mobidam.integration.service.SstManagementIntegrationService;
import de.muenchen.mobidam.mobilithek.MobilithekEaiRouteBuilder;
import de.muenchen.mobidam.security.MimeTypeProcessor;
import de.muenchen.mobidam.sstmanagment.EreignisTyp;
import org.apache.camel.*;
import org.apache.camel.builder.AdviceWith;
import org.apache.camel.builder.ExchangeBuilder;
import org.apache.camel.component.aws2.s3.AWS2S3Constants;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.test.spring.junit5.CamelSpringBootTest;
import org.apache.camel.test.spring.junit5.UseAdviceWith;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.isA;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
@SpringBootTest
@CamelSpringBootTest
@UseAdviceWith
class MobilithekRouteS3Test {

    @Autowired
    private CamelContext camelContext;

    @Produce(MobilithekEaiRouteBuilder.MOBIDAM_S3_ROUTE)
    private ProducerTemplate startMobilithekInfoRequest;

    @Autowired
    private Interfaces interfaces;

    @EndpointInject("mock:mobilithekInfo")
    private MockEndpoint mobilithekInfo;

    @EndpointInject("mock:s3Destination")
    private MockEndpoint s3Destination;

    @MockBean
    private WebClient wb; // Mock WebClient in dependency mobidam-sst-management-integration-starter

    @MockBean
    private SstManagementIntegrationService sstService;

    @MockBean
    private MimeTypeProcessor mimeTypeProcessor;

    @Captor
    private ArgumentCaptor<DatentransferCreateDTO> datentransferCaptor;

    @MockBean
    private S3CredentialProvider s3CredentialProvider;

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    void test_RouteMobilithekInfoToS3Success() throws Exception {

        startMobilithekInfoRequest.start();
        AdviceWith.adviceWith(camelContext, MobilithekEaiRouteBuilder.MOBIDAM_ROUTE_ID,
                a -> a.weaveById(MobilithekEaiRouteBuilder.MOBIDAM_ENDPOINT_S3_ID).replace().toD("mock:s3Destination"));
        camelContext.start();

        var mobilithekRequest = ExchangeBuilder.anExchange(camelContext)
                .withHeader(Constants.INTERFACE_TYPE, this.interfaces.getInterfaces().get(this.interfaces.getInterfaces().keySet().iterator().next()))
                .build();

        Mockito.when(sstService.isActivated("999fcf2d-25bb-4fa9-85ff-f7ed12349999")).thenReturn(true);

        Mockito.doAnswer(invocation -> {
            Exchange exchange = invocation.getArgument(0);
            exchange.getMessage().setHeader("accessKey", "foo");
            exchange.getMessage().setHeader("secretKey", "foo");
            ;
            return null;
        }).when(s3CredentialProvider).process(any(Exchange.class));

        mobilithekInfo.whenAnyExchangeReceived(new MobilithekInfoMock());
        s3Destination.expectedMessageCount(1);

        startMobilithekInfoRequest.send(mobilithekRequest);
        s3Destination.assertIsSatisfied();
        var exchange = s3Destination.getExchanges().get(0);

        String content = new String(exchange.getIn().getBody(InputStream.class).readAllBytes(), StandardCharsets.UTF_8);
        Assertions.assertTrue(content.contains("<startOfPeriod>2024-05-14T00:00:00</startOfPeriod><endOfPeriod>2024-05-14T23:59:00</endOfPeriod>"));

        Assertions.assertEquals("int-mdasc-mdasdev", exchange.getIn().getHeader(CommonConstants.HEADER_BUCKET_NAME));
        Assertions.assertEquals("foo", exchange.getIn().getHeader(CommonConstants.HEADER_ACCESS_KEY));
        Assertions.assertEquals("foo", exchange.getIn().getHeader(CommonConstants.HEADER_SECRET_KEY));
        Assertions.assertTrue(exchange.getIn().getHeader(AWS2S3Constants.KEY, String.class).startsWith("MDAS/Mobilithek/PR-statisch/"));
        Assertions.assertTrue(exchange.getIn().getHeader(AWS2S3Constants.KEY, String.class).endsWith("-pr-daten.xml"));

        Mockito.verify(this.sstService, Mockito.times(1)).isActivated("999fcf2d-25bb-4fa9-85ff-f7ed12349999");
        Mockito.verify(this.sstService, Mockito.times(3)).logDatentransfer(datentransferCaptor.capture());
        Assertions.assertEquals(EreignisTyp.BEGINN.name(), datentransferCaptor.getAllValues().get(0).getEreignis());
        Assertions.assertEquals(EreignisTyp.ERFOLG.name(), datentransferCaptor.getAllValues().get(1).getEreignis());
        Assertions.assertEquals(EreignisTyp.ENDE.name(), datentransferCaptor.getAllValues().get(2).getEreignis());

        startMobilithekInfoRequest.stop();
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    void test_RouteMobilithekInfoToS3Deactivated() throws Exception {

        startMobilithekInfoRequest.start();
        AdviceWith.adviceWith(camelContext, MobilithekEaiRouteBuilder.MOBIDAM_ROUTE_ID,
                a -> a.weaveById(MobilithekEaiRouteBuilder.MOBIDAM_ENDPOINT_S3_ID).replace().toD("mock:s3Destination"));
        camelContext.start();

        var mobilithekRequest = ExchangeBuilder.anExchange(camelContext)
                .withHeader(Constants.INTERFACE_TYPE, this.interfaces.getInterfaces().get(this.interfaces.getInterfaces().keySet().iterator().next()))
                .build();

        Mockito.when(sstService.isActivated("999fcf2d-25bb-4fa9-85ff-f7ed12349999")).thenReturn(false);

        startMobilithekInfoRequest.send(mobilithekRequest);

        Mockito.verify(this.sstService, Mockito.times(1)).isActivated("999fcf2d-25bb-4fa9-85ff-f7ed12349999");
        Mockito.verify(this.sstService, Mockito.times(0)).logDatentransfer(datentransferCaptor.capture());

        startMobilithekInfoRequest.stop();

    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    void test_RouteMobilithekInfoToS3Error() throws Exception {

        startMobilithekInfoRequest.start();
        AdviceWith.adviceWith(camelContext, MobilithekEaiRouteBuilder.MOBIDAM_ROUTE_ID,
                a -> a.weaveById(MobilithekEaiRouteBuilder.MOBIDAM_ENDPOINT_S3_ID).replace().toD("mock:s3Destination"));
        camelContext.start();

        var mobilithekRequest = ExchangeBuilder.anExchange(camelContext)
                .withHeader(Constants.INTERFACE_TYPE, this.interfaces.getInterfaces().get(this.interfaces.getInterfaces().keySet().iterator().next()))
                .build();

        Mockito.when(sstService.isActivated("999fcf2d-25bb-4fa9-85ff-f7ed12349999")).thenReturn(true);
        Mockito.doThrow(new MobidamException("Bucket not configured: int-mdasc-mdasdev")).when(s3CredentialProvider).process(any(Exchange.class));

        startMobilithekInfoRequest.send(mobilithekRequest);

        Mockito.verify(this.sstService, Mockito.times(1)).isActivated("999fcf2d-25bb-4fa9-85ff-f7ed12349999");
        Mockito.verify(this.sstService, Mockito.times(3)).logDatentransfer(datentransferCaptor.capture());
        Assertions.assertEquals(EreignisTyp.BEGINN.name(), datentransferCaptor.getAllValues().get(0).getEreignis());
        Assertions.assertEquals(EreignisTyp.FEHLER.name(), datentransferCaptor.getAllValues().get(1).getEreignis());
        Assertions.assertEquals(EreignisTyp.ENDE.name(), datentransferCaptor.getAllValues().get(2).getEreignis());
        Assertions.assertEquals("End interface with error : Bucket not configured: int-mdasc-mdasdev", datentransferCaptor.getAllValues().get(1).getInfo());

        startMobilithekInfoRequest.stop();

    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    void test_RouteMobilithekInfoToSecurityException() throws Exception {

        startMobilithekInfoRequest.start();
        AdviceWith.adviceWith(camelContext, MobilithekEaiRouteBuilder.MOBIDAM_ROUTE_ID,
                a -> a.weaveById(MobilithekEaiRouteBuilder.MOBIDAM_ENDPOINT_S3_QUARANTINE_ID).replace().toD("mock:s3Destination"));
        camelContext.start();

        var mobilithekRequest = ExchangeBuilder.anExchange(camelContext)
                .withHeader(Constants.INTERFACE_TYPE, this.interfaces.getInterfaces().get(this.interfaces.getInterfaces().keySet().iterator().next()))
                .build();

        Mockito.when(sstService.isActivated("999fcf2d-25bb-4fa9-85ff-f7ed12349999")).thenReturn(true);

        Mockito.doThrow(new MobidamSecurityException("danger!")).when(mimeTypeProcessor).process(isA(Exchange.class));

        startMobilithekInfoRequest.send(mobilithekRequest);

        Mockito.verify(this.sstService, Mockito.times(1)).isActivated("999fcf2d-25bb-4fa9-85ff-f7ed12349999");
        Mockito.verify(this.sstService, Mockito.times(3)).logDatentransfer(datentransferCaptor.capture());
        Assertions.assertEquals(EreignisTyp.BEGINN.name(), datentransferCaptor.getAllValues().get(0).getEreignis());
        Assertions.assertEquals(EreignisTyp.FEHLER.name(), datentransferCaptor.getAllValues().get(1).getEreignis());
        Assertions.assertEquals(EreignisTyp.ENDE.name(), datentransferCaptor.getAllValues().get(2).getEreignis());

        s3Destination.expectedMessageCount(1);
        startMobilithekInfoRequest.stop();

    }

}
