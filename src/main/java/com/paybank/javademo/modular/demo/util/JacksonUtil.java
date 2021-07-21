package com.paybank.javademo.modular.demo.util;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonParser.Feature;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JacksonUtil {

	private static final Logger log = LoggerFactory.getLogger(JacksonUtil.class);

	private static final ObjectMapper OBJECT_MAPPER;

	static {
		OBJECT_MAPPER = new ObjectMapper();
		OBJECT_MAPPER.configure(MapperFeature.AUTO_DETECT_FIELDS, true);
		OBJECT_MAPPER.configure(DeserializationFeature.FAIL_ON_NULL_FOR_PRIMITIVES, false);
		OBJECT_MAPPER.configure(DeserializationFeature.READ_ENUMS_USING_TO_STRING, true);
		OBJECT_MAPPER.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		OBJECT_MAPPER.configure(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES, false);
		OBJECT_MAPPER.configure(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT, true);
		OBJECT_MAPPER.configure(DeserializationFeature.READ_DATE_TIMESTAMPS_AS_NANOSECONDS, true);
		OBJECT_MAPPER.configure(DeserializationFeature.READ_UNKNOWN_ENUM_VALUES_AS_NULL, true);
		OBJECT_MAPPER.configure(DeserializationFeature.READ_ENUMS_USING_TO_STRING, true);
		OBJECT_MAPPER.configure(DeserializationFeature.READ_ENUMS_USING_TO_STRING, true);
		OBJECT_MAPPER.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
		OBJECT_MAPPER.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
		OBJECT_MAPPER.configure(SerializationFeature.WRITE_ENUMS_USING_TO_STRING, true);
		OBJECT_MAPPER.configure(SerializationFeature.WRITE_DATE_TIMESTAMPS_AS_NANOSECONDS, true);
		OBJECT_MAPPER.configure(SerializationFeature.FLUSH_AFTER_WRITE_VALUE, true);
		OBJECT_MAPPER.setSerializationInclusion(Include.NON_EMPTY);
		OBJECT_MAPPER.configure(SerializationFeature.INDENT_OUTPUT, false);
		OBJECT_MAPPER.configure(Feature.ALLOW_SINGLE_QUOTES, true) ;
		
	}

	public static String obj2json(Object obj) {
		if (null == obj) {
			return null;
		}

		try {
			return OBJECT_MAPPER.writeValueAsString(obj);
		} catch (Exception e) {
			log.error("Json Parsing failed：{}", e.getMessage());
			return null;
		}

	}
}
