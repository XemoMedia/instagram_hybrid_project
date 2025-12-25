package com.xmedia.social.schedular.media;

import org.springframework.stereotype.Service;

import com.xmedia.social.schedular.dispatcher.SocialPlatformPoster;
import com.xmedia.social.schedular.to.ScheduledPostTO;
@Service
public class FacebookPoster implements SocialPlatformPoster {

	@Override
	public void post(ScheduledPostTO post) {
		System.out.println("Posting to FACEBOOK: " + post.getCaption());
		
	}

}
