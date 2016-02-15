package org.foo.button.dao.impl;

import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * Created by phil on 2/14/16.
 */
public class TimeUtils {

    public static final Long A_MINUTE_FROM_NOW = 1L * 1000L * 60L;
    public static final Long A_MINUTE_AGO = -A_MINUTE_FROM_NOW;

    public static final Long A_WEEK_FROM_NOW = A_MINUTE_FROM_NOW * 60L * 24L * 7L;
    public static final Long A_WEEK_AGO = -A_WEEK_FROM_NOW;

    public static Date dateFromNow(Long diff) {
        long now = new Date().getTime();
        return new Date(now+diff);
    }

    public static Date now() {
        return new Date();
    }

    public static Date aMinuteAgo() {
        return minutesAgo(1);
    }

    public static Date minutesAgo(int i) {
        return dateFromNow(i*A_MINUTE_AGO);
    }
}
