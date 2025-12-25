package com.xmedia.social.schedular.dispatcher;

import java.util.Map;

import org.springframework.stereotype.Service;

import com.xmedia.social.schedular.enm.SocialPlatformEnum;
import com.xmedia.social.schedular.media.FacebookPoster;
import com.xmedia.social.schedular.media.InstagramPoster;
import com.xmedia.social.schedular.media.YouTubePoster;
import com.xmedia.social.schedular.to.ScheduledPostTO;
import com.xmedia.social.schedular.to.SocialDetailsTO;

@Service
public class SocialPostDispatcher {
	private final Map<SocialPlatformEnum, SocialPlatformPoster> posterMap;
	public SocialPostDispatcher(
			FacebookPoster facebook,
            InstagramPoster instagram,
            YouTubePoster youtube
    ) {
        posterMap = Map.of(
        		SocialPlatformEnum.FACEBOOK, facebook,
        		SocialPlatformEnum.INSTAGRAM, instagram,
        		SocialPlatformEnum.YOUTUBE, youtube
        );
    }
	
	public void dispatch(ScheduledPostTO post) {
        for (SocialDetailsTO sd : post.getPlatforms()) {
            SocialPlatformPoster poster = posterMap.get(sd.getPlatform());
            if (poster == null) {
                throw new IllegalStateException(
                    "Unsupported platform: " + sd.getPlatform()
                );
            }
            poster.post(post);
        }
    }
	
}