package com.xmedia.social.upload.service;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.xmedia.social.base.enums.DataSource;

@Component
public class FileConverterFactory {

	private final Map<DataSource, FileConversionService> strategies;

	public FileConverterFactory(List<FileConversionService> converters) {
		this.strategies = converters.stream()
				.collect(Collectors.toUnmodifiableMap(FileConversionService::getSupportedSource, Function.identity()));
	}

	public FileConversionService getConverter(DataSource dataSource) {
		FileConversionService converter = strategies.get(dataSource);
		if (converter == null) {
			throw new IllegalArgumentException("No converter registered for dataSource: " + dataSource);
		}
		return converter;
	}
}

