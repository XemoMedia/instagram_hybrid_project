/*
 * package com.example.instagram.entity;
 * 
 * import com.fasterxml.jackson.databind.JsonNode; import
 * com.vladmihalcea.hibernate.type.json.JsonType; import jakarta.persistence.*;
 * import org.hibernate.annotations.Type;
 * 
 * @Entity
 * 
 * @Table(name = "instagram_media") public class InstagramMediaEntity {
 * 
 * @Id
 * 
 * @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
 * 
 * private String instagramId;
 * 
 * @Type(JsonType.class)
 * 
 * @Column(columnDefinition = "jsonb") private JsonNode jsonData;
 * 
 * public Long getId() { return id; }
 * 
 * public void setId(Long id) { this.id = id; }
 * 
 * public String getInstagramId() { return instagramId; }
 * 
 * public void setInstagramId(String instagramId) { this.instagramId =
 * instagramId; }
 * 
 * public JsonNode getJsonData() { return jsonData; }
 * 
 * public void setJsonData(JsonNode jsonData) { this.jsonData = jsonData; } }
 */