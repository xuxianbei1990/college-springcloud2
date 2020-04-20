package college.springcloud.common.serialize;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;
import java.math.BigDecimal;

/**
 * @author: xuxianbei
 * Date: 2020/4/3
 * Time: 14:55
 * Version:V1.0
 */
public class InNumberMultiply100JS extends JsonDeserializer<Number> {

    @Override
    public Number deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        return BigDecimal.valueOf(p.getDoubleValue()).multiply(BigDecimal.valueOf(100)).intValue();
    }
}