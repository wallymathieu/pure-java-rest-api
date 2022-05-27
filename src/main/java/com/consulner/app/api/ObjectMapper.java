package com.consulner.app.api;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.util.List;

public interface ObjectMapper {
	byte[] writeValueAsBytes(Object pO) throws IOException;

	<T> T readValue(InputStream pIs, Class<T> pType) throws IOException;
	<T> T readValue(Reader r, Class<T> pType) throws IOException;
	<T> List<T> readValues(Reader r, Class<T> pType) throws IOException;
}
