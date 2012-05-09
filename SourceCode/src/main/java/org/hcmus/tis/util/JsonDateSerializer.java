package org.hcmus.tis.util;

import java.io.IOException;
import java.util.Date;

import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.JsonSerializer;
import org.codehaus.jackson.map.SerializerProvider;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.context.i18n.LocaleContextHolder;

public class JsonDateSerializer extends JsonSerializer<Date> {
	private static final DateTimeFormatter dateTimeFormater = DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ss");

	@Override
	public void serialize(Date date, JsonGenerator gen, SerializerProvider provider)
			throws IOException, JsonProcessingException {
		String formattedDate = dateTimeFormater.withLocale(LocaleContextHolder.getLocale()).print(date.getTime());
		gen.writeString(formattedDate);
		
	}
}
