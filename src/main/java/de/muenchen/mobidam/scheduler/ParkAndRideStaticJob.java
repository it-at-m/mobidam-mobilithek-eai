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
public class ParkAndRideStaticJob implements Job {

    private CamelContext camelContext;

    private Interfaces interfaces;

    @Produce(MobilithekEaiRouteBuilder.MOBIDAM_S3_ROUTE)
    private ProducerTemplate producer;

    public void execute(JobExecutionContext context) throws JobExecutionException {

        log.info("Scheduler starts park and ride static request : " + context.getFireTime().toString());

        var mobilithekRequest = ExchangeBuilder.anExchange(this.camelContext)
                .withHeader(Constants.INTERFACE_TYPE, this.interfaces.getInterfaces().get(Constants.PARK_RIDE_STATIC_DATA))
                .build();

        producer.send(mobilithekRequest);
    }

}
