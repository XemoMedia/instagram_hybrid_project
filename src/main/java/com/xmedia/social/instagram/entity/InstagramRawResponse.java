package com.xmedia.social.instagram.entity;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "instagram_raw_response")
public class InstagramRawResponse {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "instagram_id")
    private String instagramId;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "json_data", columnDefinition = "jsonb")
    private String jsonData;
}
