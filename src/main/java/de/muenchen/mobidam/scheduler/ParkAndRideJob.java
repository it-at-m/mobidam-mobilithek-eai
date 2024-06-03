package de.muenchen.mobidam.scheduler;

import de.muenchen.mobidam.Constants;
import de.muenchen.mobidam.config.Interfaces;
import de.muenchen.mobidam.mobilithek.MobilithekEaiRouteBuilder;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.CamelContext;
import org.apache.camel.Produce;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.ExchangeBuilder;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@AllArgsConstructor
public class ParkAndRideJob implements Job {

    private CamelContext camelContext;

    private Interfaces interfaces;

    @Produce(MobilithekEaiRouteBuilder.MOBIDAM_S3_ROUTE)
    private ProducerTemplate producer;

    public void execute(JobExecutionContext context) throws JobExecutionException {

        log.info("Scheduler starts park and ride request : " + context.getFireTime().toString());

        var mobilithekRequest = ExchangeBuilder.anExchange(this.camelContext)
                .withHeader(Constants.INTERFACE_TYPE, this.interfaces.getInterfaces().get(Constants.PARK_RIDE_DATA))
                .build();

        producer.send(mobilithekRequest);
    }

}
