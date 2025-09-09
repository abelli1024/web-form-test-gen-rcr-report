package mo.must.chat.common.util;

import org.apache.commons.lang3.StringUtils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;


public class DateUtil {

    public static String DATE_FORMAT_FULL_STRING = "yyyy-MM-dd HH:mm:ss";
    public static String DATE_FORMAT_DATE_STRING = "yyyy-MM-dd";
    public static String DATE_FORMAT_TIME_STRING = "HH:mm:ss";

    public static Date currDate() {
        return new Date();
    }
    public static Date parse(String source, String pattern) {
        return parse(source, new SimpleDateFormat(pattern));
    }

    public static Date parse(String source, SimpleDateFormat sdf) {
        if (StringUtils.isEmpty(source)) {
            return null;
        }
        try {
            return sdf.parse(source);
        } catch (Exception e) {
            throw new IllegalArgumentException(e.getMessage(), e);
        }
    }

    public static String format(Date date, String pattern) {
        return format(date, new SimpleDateFormat(pattern));
    }

    public static String format(Date date, DateFormat df) {
        if (date == null) {
            return null;
        }
        return df.format(date);
    }

    public static String format(String srcDate, String srcPattern, String descPattern) {
        return format(parse(srcDate, srcPattern), descPattern);
    }

    public static String format(Date date) {
        if (date == null) {
            return null;
        }

        if (String.valueOf(date.getTime()).endsWith("00000")) {
            return format(date, new SimpleDateFormat(DATE_FORMAT_DATE_STRING));
        } else if (date.getYear() == 70 && date.getMonth() == 0 && date.getDate() == 1) { // 1970-1-1
            return format(date, new SimpleDateFormat(DATE_FORMAT_TIME_STRING));
        } else {
            return format(date, new SimpleDateFormat(DATE_FORMAT_FULL_STRING));
        }
    }

    public static Date utcDateConverter(String date) {
        SimpleDateFormat isoFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        isoFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        try {
            String dateStringMilliseconds = date.substring(0, 23) + "Z";
            return isoFormat.parse(dateStringMilliseconds);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

}
