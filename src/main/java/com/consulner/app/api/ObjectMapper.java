package com.consulner.app.api;

import java.io.IOException;
import java.io.InputStream;

public interface ObjectMapper {
	byte[] writeValueAsBytes(Object pO) throws IOException;

	<T> T readValue(InputStream pIs, Class<T> pType) throws IOException;
}
