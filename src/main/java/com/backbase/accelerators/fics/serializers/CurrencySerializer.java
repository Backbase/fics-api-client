package com.backbase.accelerators.fics.serializers;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.math.BigDecimal;

public class CurrencySerializer extends JsonDeserializer<BigDecimal> {

    @Override
    public BigDecimal deserialize(
            JsonParser jsonParser,
            DeserializationContext deserializationContext) throws IOException, JsonProcessingException {

        var value = jsonParser.getValueAsString();
        if (StringUtils.isNotBlank(value)) {
            return new BigDecimal(value.replace(",", ""));
        }

        return null;
    }
}
