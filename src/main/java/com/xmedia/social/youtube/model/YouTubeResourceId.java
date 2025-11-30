package com.xmedia.social.youtube.model;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "you_tube_resource_id", uniqueConstraints = @UniqueConstraint(columnNames = {"kind"}))
public class YouTubeResourceId {

    @Id
    private String id;

    @Column(unique = true)
    private String kind;

    private String videoId;
    private String channelId;
    private String playlistId;

    @OneToMany(mappedBy = "resourceId", cascade = CascadeType.ALL)
    private List<YouTubeSearchResult> searchResults;
}

