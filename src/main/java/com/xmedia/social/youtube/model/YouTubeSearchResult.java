package com.xmedia.social.youtube.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class YouTubeSearchResult {
    @Id
    private String id;
    private String kind;
    private String etag;
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "resource_id_kind", referencedColumnName = "kind")
    private YouTubeResourceId resourceId;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "snippet_id", referencedColumnName = "id")
    private YouTubeSearchSnippet snippet;
}
