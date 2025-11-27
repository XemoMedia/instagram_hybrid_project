package com.xmedia.social.youtube.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
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
@Table(name = "youtube_thumbnail_details")
public class YouTubeThumbnailDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "default_thumbnail_id", referencedColumnName = "id")
    private YouTubeThumbnail defaultThumbnail;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "medium_thumbnail_id", referencedColumnName = "id")
    private YouTubeThumbnail medium;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "high_thumbnail_id", referencedColumnName = "id")
    private YouTubeThumbnail high;
}
