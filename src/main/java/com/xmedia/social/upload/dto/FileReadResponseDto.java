package com.xmedia.social.upload.dto;

import java.util.List;
import java.util.Map;

import com.xmedia.social.base.enums.DataSource;

public record FileReadResponseDto(
		DataSource dataSource,
		int recordCount,
		List<Map<String, String>> records) {
}

