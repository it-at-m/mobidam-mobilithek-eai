package de.muenchen.mobidam.security;

import java.io.InputStream;

public interface MaliciousCodeDetector {

    boolean isValidData(final InputStream stream) throws Exception;
}
