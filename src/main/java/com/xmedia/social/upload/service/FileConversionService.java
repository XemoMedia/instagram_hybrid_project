package com.xmedia.social.upload.service;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

import com.xmedia.social.base.enums.DataSource;

public interface FileConversionService {

	DataSource getSupportedSource();

	List<Map<String, String>> convert(InputStream inputStream) throws IOException;
}

