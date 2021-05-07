package com.bitozen.zencamp.backend.common.util;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

public class DateHandler extends StdDeserializer<Date>{

	public DateHandler() {
	    this(null);
	  }

	  public DateHandler(Class<?> clazz) {
	    super(clazz);
	  }

	  @Override
	  public Date deserialize(JsonParser jsonparser, DeserializationContext context)
	      throws IOException, JsonProcessingException {
	    String date = jsonparser.getText();
	    String datePattern = "\\d{2}-\\d{2}-\\d{4}";
	    Date dateResult;
	    try {
	    	if(date.matches(datePattern)) {
	    		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
	    		dateResult = sdf.parse(date);
	    	} else {
	    		SimpleDateFormat ts = new SimpleDateFormat("E MMM dd HH:mm:ss z yyyy");
	    		dateResult = ts.parse(date);
	    	}
    		return dateResult;
	      
	    } catch (Exception e) {
	    	e.printStackTrace();
	      return null;
	    }
	  }
}
