package org.sun.racing.util;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;

public class Utils {
    public static final String DATETIME_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.000z";

    private Utils() {
        // not supposed to be instantiated
    }

    public static ZonedDateTime getCurrentDateTime() {
        return ZonedDateTime.now(ZoneOffset.UTC);
    }
}
