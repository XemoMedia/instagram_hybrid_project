package com.xmedia.social.schedular.dispatcher;

import com.xmedia.social.schedular.to.ScheduledPostTO;

public interface SocialPlatformPoster {
	 void post(ScheduledPostTO post);
}
