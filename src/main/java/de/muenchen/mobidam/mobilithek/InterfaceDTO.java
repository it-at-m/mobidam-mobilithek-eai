package de.muenchen.mobidam.mobilithek;

import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class InterfaceDTO {

    private UUID mobidamSstId;
    private String mobilithekSubscriptionId;
    private String mobilithekUrl;
    private String cronExpression;
    private String s3ObjectPath;
    private String s3DateFormat;
    private String s3Bucket;

}
