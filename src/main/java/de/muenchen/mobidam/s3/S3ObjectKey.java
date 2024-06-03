package de.muenchen.mobidam.s3;

import de.muenchen.mobidam.Constants;
import de.muenchen.mobidam.mobilithek.InterfaceDTO;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.component.aws2.s3.AWS2S3Constants;
import org.springframework.stereotype.Component;

@Component
public class S3ObjectKey implements Processor {

    @Override
    public void process(Exchange exchange) throws Exception {
        var mobilithekInterface = exchange.getIn().getHeader(Constants.INTERFACE_TYPE, InterfaceDTO.class);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(mobilithekInterface.getS3DateFormat());
        String date = simpleDateFormat.format(new Date());
        exchange.getIn().setHeader(AWS2S3Constants.KEY, String.format(mobilithekInterface.getS3ObjectPath(), date));
    }
}
