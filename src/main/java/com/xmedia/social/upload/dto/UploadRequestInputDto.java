package com.xmedia.social.upload.dto;

import org.springframework.web.multipart.MultipartFile;

import com.xmedia.social.base.enums.DataSource;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UploadRequestInputDto {

    @NotNull
    private DataSource dataSource;

    @NotNull
    private MultipartFile file;
}
