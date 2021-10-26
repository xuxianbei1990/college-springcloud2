package college.springcloud.config;

import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jsr310.PackageVersion;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalTimeSerializer;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

/**
 * java 8 时间默认序列化
 *
 * @author weishili
 */
public class SwordfishJavaTimeModule extends SimpleModule {
    private static final long serialVersionUID = -4097116964556392112L;

    public static final String PATTERN_DATE_TIME = "yyyy-MM-dd HH:mm:ss";
    public static final String PATTERN_DATE = "yyyy-MM-dd";
    public static final String PATTERN_TIME = "HH:mm:ss";

    public static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern(PATTERN_DATE_TIME);
    public static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern(PATTERN_DATE);
    public static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern(PATTERN_TIME);

    public SwordfishJavaTimeModule() {
        super(PackageVersion.VERSION);
        this.addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer(DATE_TIME_FORMATTER));
        this.addDeserializer(LocalDate.class, new LocalDateDeserializer(DATE_FORMATTER));
        this.addDeserializer(LocalTime.class, new LocalTimeDeserializer(TIME_FORMATTER));
        this.addSerializer(LocalDateTime.class, new LocalDateTimeSerializer(DATE_TIME_FORMATTER));
        this.addSerializer(LocalDate.class, new LocalDateSerializer(DATE_FORMATTER));
        this.addSerializer(LocalTime.class, new LocalTimeSerializer(TIME_FORMATTER));
    }
}
