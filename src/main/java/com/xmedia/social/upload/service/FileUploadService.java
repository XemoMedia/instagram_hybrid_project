package com.xmedia.social.upload.service;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.xmedia.social.base.enums.DataSource;
import com.xmedia.social.upload.dto.FileReadResponseDto;
import com.xmedia.social.upload.dto.UploadRequestInputDto;
import com.xmedia.social.upload.entity.SocialCommentAnalysis;
import com.xmedia.social.upload.repository.SocialCommentAnalysisRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FileUploadService {

	private final FileConverterFactory fileConverterFactory;
	private final ResourceLoader resourceLoader;
	private final SocialCommentAnalysisRepository socialCommentAnalysisRepository;
	public FileReadResponseDto read(DataSource dataSource, MultipartFile file, String resourcePath) throws IOException {
		String resolvedResourcePath = resourcePath;

		if ((file == null || file.isEmpty()) && !StringUtils.hasText(resolvedResourcePath)) {
			throw new IllegalArgumentException("Either a file or a resourcePath must be provided.");
		}

		try (InputStream inputStream = obtainInputStream(file, resolvedResourcePath)) {
			List<Map<String, String>> records = locateConverter(dataSource).convert(inputStream);
			return new FileReadResponseDto(dataSource, records.size(), records);
		}
	}

	public FileReadResponseDto readDefault(DataSource dataSource) throws IOException {
		return read(dataSource, null, null);
	}

	public int uploadFileData(UploadRequestInputDto uploadRequest) throws IOException {
		validateUploadRequest(uploadRequest);

		 List<Map<String, String>> records;
		    try (InputStream inputStream = uploadRequest.getFile().getInputStream()) {
		        records = locateConverter(uploadRequest.getDataSource()).convert(inputStream);
		    }

		    if (records == null || records.isEmpty()) {
		        return 0;
		    }

		    // 1️⃣ Extract msgIds from Excel
		    Set<String> excelMsgIds = records.stream()
		            .map(this::normalizeKeys)
		            .map(m -> value(m, "msgid"))
		            .filter(Objects::nonNull)
		            .collect(Collectors.toSet());

		    // 2️⃣ Find existing msgIds in DB
		    Set<String> existingMsgIds =
		    		socialCommentAnalysisRepository.findExistingMsgIds(excelMsgIds);


		    // 3️⃣ Save only NEW records
		    List<SocialCommentAnalysis> entities = records.stream()
		            .map(this::mapRecordToSocialCommentAnalysis)
		            .filter(e -> !existingMsgIds.contains(e.getMsgId()))
		            .toList();

		    socialCommentAnalysisRepository.saveAll(entities);
		    return entities.size();
		}


	public void deleteAllSocialCommentAnalysis() {
		socialCommentAnalysisRepository.deleteAllInBatch();
	}

	private InputStream obtainInputStream(MultipartFile file, String resourcePath) throws IOException {
		if (file != null && !file.isEmpty()) {
			return file.getInputStream();
		}

		if (StringUtils.hasText(resourcePath)) {
			String resolvedPath = resourcePath.startsWith("classpath:") ? resourcePath : "classpath:" + resourcePath;
			Resource resource = resourceLoader.getResource(resolvedPath);
			if (!resource.exists() || !resource.isReadable()) {
				throw new FileNotFoundException("Resource not found: " + resolvedPath);
			}
			return resource.getInputStream();
		}

		throw new IllegalArgumentException("Either an uploaded file or a resourcePath must be supplied.");
	}

	private FileConversionService locateConverter(DataSource dataSource) {
		return fileConverterFactory.getConverter(dataSource);
	}

	private void validateUploadRequest(UploadRequestInputDto uploadRequest) {
		if (uploadRequest == null || uploadRequest.getDataSource() == null) {
			throw new IllegalArgumentException("Data source must be provided.");
		}

		MultipartFile file = uploadRequest.getFile();
		if (file == null || file.isEmpty()) {
			throw new IllegalArgumentException("A non-empty file must be supplied for upload.");
		}
	}

	private SocialCommentAnalysis mapRecordToSocialCommentAnalysis(Map<String, String> record) {
		Map<String, String> normalized = normalizeKeys(record);
		String commentId = valueOrDefault(normalized, "id", UUID.randomUUID().toString());
		
	    return SocialCommentAnalysis.builder()
                .id(commentId)
                .msgId(value(normalized,"msgid"))
                .username(value(normalized, "username"))
                .comment(value(normalized, "comment"))
                .platform(value(normalized, "platform"))
                .brand(value(normalized, "brand"))
                .build();
	}

	private Map<String, String> normalizeKeys(Map<String, String> record) {
		Map<String, String> normalized = new LinkedHashMap<>();
		if (record == null) {
			return normalized;
		}

		record.forEach((key, value) -> {
			if (key != null) {
				normalized.put(key.trim().toLowerCase(Locale.ROOT), value);
			}
		});
		return normalized;
	}

	private String value(Map<String, String> record, String key) {
		if (record == null || key == null) {
			return null;
		}
		String raw = record.get(key);
		return StringUtils.hasText(raw) ? raw.trim() : null;
	}

	private String valueOrDefault(Map<String, String> record, String key, String defaultValue) {
		String value = value(record, key);
		return value != null ? value : defaultValue;
	}
}


