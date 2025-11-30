package com.xmedia.social.youtube.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class PageInfo {
    private Integer totalResults;
    private Integer resultsPerPage;
}
