package com.xmedia.social.upload.service;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.stereotype.Service;

import com.xmedia.social.base.enums.DataSource;

@Service
public class CsvFileConversionService implements FileConversionService {

	private static final CSVFormat DEFAULT_FORMAT = CSVFormat.DEFAULT.builder()
			.setSkipHeaderRecord(true)
			.setHeader()
			.build();

	@Override
	public DataSource getSupportedSource() {
		return DataSource.CSV;
	}

	@Override
	public List<Map<String, String>> convert(InputStream inputStream) throws IOException {
		try (Reader reader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
			 CSVParser parser = new CSVParser(reader, DEFAULT_FORMAT)) {

			List<String> headers = parser.getHeaderNames();
			List<Map<String, String>> rows = new ArrayList<>();

			for (CSVRecord record : parser) {
				Map<String, String> row = new LinkedHashMap<>();
				for (String header : headers) {
					row.put(header, record.get(header));
				}
				rows.add(row);
			}

			return rows;
		}
	}
}

