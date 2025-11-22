package com.xmedia.social.instagram.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

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
