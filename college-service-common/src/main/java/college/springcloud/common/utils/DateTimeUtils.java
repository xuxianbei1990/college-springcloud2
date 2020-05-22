package college.springcloud.common.utils;

import java.time.*;
import java.util.Date;

/**
 * @author: xuxianbei
 * Date: 2020/5/21
 * Time: 18:24
 * Version:V1.0
 */
public class DateTimeUtils {
    private DateTimeUtils(){

    }

    /**
     * Date 转换 LocalDateTime
     * @param date
     * @return
     */
    public static LocalDateTime dateToLocalDateTime(Date date){
        Instant instant = date.toInstant();
        ZoneId zoneId = ZoneId.systemDefault();

        return instant.atZone(zoneId).toLocalDateTime();
    }

    /**
     * LocalDateTime 转换  Date
     * @param dateTime
     * @return
     */
    public static Date localDateTimeToDate(LocalDateTime dateTime){
        ZonedDateTime zdt = dateTime.atZone(ZoneId.systemDefault());
        return Date.from(zdt.toInstant());
    }

    /**l
     * LocalDateTime 转换  Date
     * @param localDate
     * @return
     */
    public static Date localDateTimeToDate(LocalDate localDate){
        ZonedDateTime zdt = localDate.atStartOfDay(ZoneId.systemDefault());
        return Date.from(zdt.toInstant());
    }

    public static void main(String[] args) {
        System.out.println(dateToLocalDateTime(new Date()));
        System.out.println(localDateTimeToDate(LocalDateTime.now()));
    }
}
