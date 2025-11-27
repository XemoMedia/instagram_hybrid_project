package com.xmedia.social.youtube.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "youtube_search_data")
public class YouTubeSearchData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "youtube_token_id", nullable = false)
    private YouTubeToken youTubeToken;

    private String kind;
    private String etag;
    private String regionCode;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "page_info_id", referencedColumnName = "id")
    private YouTubePageInfo pageInfo;

    @OneToMany(mappedBy = "youTubeSearchData", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<YouTubeSearchItem> items;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}
