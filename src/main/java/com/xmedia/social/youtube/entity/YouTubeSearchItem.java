package com.xmedia.social.youtube.entity;

import com.xmedia.social.youtube.model.YouTubeSearchSnippet;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "youtube_search_item")
public class YouTubeSearchItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String kind;
    private String etag;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "youtube_search_dat-id", nullable = false)
    private YouTubeSearchData youTubeSearchData;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "search_id", referencedColumnName = "id")
    private YouTubeSearchId searchId;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "search_snippet_id", referencedColumnName = "id")
    private YouTubeSearchSnippet snippet;
}
