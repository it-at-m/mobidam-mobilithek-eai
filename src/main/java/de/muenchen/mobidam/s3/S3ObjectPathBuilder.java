package de.muenchen.mobidam.s3;

import de.muenchen.mobidam.Constants;
import de.muenchen.mobidam.mobilithek.InterfaceDTO;
import java.text.SimpleDateFormat;
import java.util.Date;

public class S3ObjectPathBuilder {

    public static String buildFilingPath(final InterfaceDTO interfaceDTO) {
        return String.format(interfaceDTO.getS3ObjectPath(), getFormattedDate(interfaceDTO.getS3DateFormat()));
    }

    public static String buildQuarantinePath(final InterfaceDTO interfaceDTO) {
        return Constants.QUARANTINE_PREFIX + String.format(interfaceDTO.getS3ObjectPath(), getFormattedDate(interfaceDTO.getS3DateFormat()));
    }

    private static String getFormattedDate(String dateFormat) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateFormat);
        return simpleDateFormat.format(new Date());
    }

}
