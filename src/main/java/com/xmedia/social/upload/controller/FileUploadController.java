package com.xmedia.social.upload.controller;

import java.io.IOException;
import java.util.Map;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.xmedia.social.base.enums.DataSource;
import com.xmedia.social.upload.dto.FileReadResponseDto;
import com.xmedia.social.upload.dto.UploadRequestInputDto;
import com.xmedia.social.upload.service.FileUploadService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/files")
@Tag(name = "File Upload", description = "Convert CSV/XLSX data into structured JSON.")
@RequiredArgsConstructor
public class FileUploadController {

	private final FileUploadService fileUploadService;

	@Operation(
			summary = "Upload comment analytics file",
			description = "Accepts a CSV or XLSX file, resolves the proper converter via strategy pattern, "
					+ "and persists the parsed rows into SocialCommentAnalysis/MediaAnalysisAnalytic tables.")
	@PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Map<String, Object>> uploadFileData(@ModelAttribute UploadRequestInputDto uploadRequest) throws IOException {
		int savedRecords = fileUploadService.uploadFileData(uploadRequest);
		return ResponseEntity.ok(Map.of("savedRecords", savedRecords));
	}

	@Operation(
			summary = "Delete all uploaded comment analysis records",
			description = "Removes every record from the social_comment_analysis table. Use with caution.")
	@DeleteMapping("/upload")
	public ResponseEntity<Map<String, Object>> deleteAllUploadedData() {
		fileUploadService.deleteAllSocialCommentAnalysis();
		return ResponseEntity.ok(Map.of("message", "All social comment analysis records deleted"));
	}

	@Operation(
			summary = "Read default resource",
			description = "Loads the default resource configured for the specified data source.")
	@GetMapping(value = "/convert/default", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<FileReadResponseDto> convertDefault(@RequestParam DataSource dataSource) throws IOException {
		return ResponseEntity.ok(fileUploadService.readDefault(dataSource));
	}
}

