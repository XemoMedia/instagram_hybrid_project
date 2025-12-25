package com.xmedia.social.schedular.to;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import lombok.Data;
@Data
public class ScheduledPostTO {
	 private String caption;
	 private String imageUrl;
	 private String videoUrl;
	 private List<SocialDetailsTO> platforms = new ArrayList<>();
	 private LocalDateTime scheduledTime;
	 private String status;
}
