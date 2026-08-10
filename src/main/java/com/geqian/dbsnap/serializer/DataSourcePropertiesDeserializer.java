package com.geqian.dbsnap.serializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.geqian.dbsnap.common.properties.AbstractDataSourceProperties;
import com.geqian.dbsnap.context.DataSourcePropertiesFactory;

import java.io.IOException;

/**
 * 数据源配置反序列化器
 *
 * @author geqian
 */
public class DataSourcePropertiesDeserializer extends JsonDeserializer<AbstractDataSourceProperties> {
    @Override
    public AbstractDataSourceProperties deserialize(JsonParser p, DeserializationContext context) throws IOException {
        try {
            ObjectMapper mapper = (ObjectMapper) p.getCodec();
            JsonNode node = mapper.readTree(p);
            String type = node.get("type").asText();
            Class<? extends AbstractDataSourceProperties> typeClass = DataSourcePropertiesFactory.getType(type);
            return mapper.treeToValue(node, typeClass);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
