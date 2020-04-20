package college.springcloud.common.serialize;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.math.BigDecimal;

/**
 * @author: xuxianbei
 * Date: 2020/4/3
 * Time: 13:59
 * Version:V1.0
 */
public class NumberJsonSerializer extends JsonSerializer<Number> {

    @Override
    public void serialize(Number number, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeNumber(BigDecimal.valueOf(number.doubleValue()).divide(BigDecimal.valueOf(100)));
    }
}
