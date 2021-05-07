package com.bitozen.zencamp.backend.common.util;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.sql.Timestamp;
import java.time.ZoneId;
import java.time.ZonedDateTime;

/**
 *
 * @author edwin
 */
@Converter(autoApply = true)
public class ZonedDateTimeAttributeConverter implements AttributeConverter<ZonedDateTime, Timestamp>  {

    @Override
    public Timestamp convertToDatabaseColumn(ZonedDateTime zoneDateTime) {
        return zoneDateTime == null ? null : Timestamp.valueOf(zoneDateTime.toLocalDateTime());
    }

    @Override
    public ZonedDateTime convertToEntityAttribute(Timestamp sqlTimestamp) {
        return sqlTimestamp == null ? null : sqlTimestamp.toInstant().atZone(ZoneId.systemDefault());
    }
    
}
