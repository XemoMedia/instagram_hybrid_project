package com.xmedia.social.youtube.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.Data;

@Entity
@Data
public class CommentThreadEntity {
	@Id
	private String id; // YouTube comment thread ID

	private String kind;
	private String etag;
	private String channelId;
	private String videoId;

	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "top_level_comment_id")
	private CommentEntity topLevelComment;

	private Boolean canReply;
	private Integer totalReplyCount;
	private Boolean isPublic;

}
