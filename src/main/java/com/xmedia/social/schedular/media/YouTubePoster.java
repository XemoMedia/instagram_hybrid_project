package com.xmedia.social.schedular.media;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

import org.springframework.stereotype.Component;
import org.springframework.util.StreamUtils;

import com.xmedia.social.schedular.dispatcher.HttpClient;
import com.xmedia.social.schedular.dispatcher.SocialPlatformPoster;
import com.xmedia.social.schedular.enm.SocialPlatformEnum;
import com.xmedia.social.schedular.to.ScheduledPostTO;
import com.xmedia.social.schedular.to.SocialDetailsTO;
@Component
public class YouTubePoster implements SocialPlatformPoster  {

	private HttpClient httpClient;
	
	String acessToken = null;

    public YouTubePoster(HttpClient httpClient) {
        this.httpClient = httpClient;
    }
    
	@Override
	public void post(ScheduledPostTO post) {
        if (post.getVideoUrl() == null) {
            throw new IllegalArgumentException("YouTube requires videoUrl");
        }

        // STEP 1: Create resumable upload session
        String uploadUrl = createUploadSession(post);

        // STEP 2: Upload video binary
        uploadVideo(uploadUrl, post);
    }

	private String createUploadSession(ScheduledPostTO post) {
		

		Optional<SocialDetailsTO> youtubeDetails = post.getPlatforms().stream().filter(p -> SocialPlatformEnum.YOUTUBE.equals(p.getPlatform())).findFirst();
		acessToken = youtubeDetails.get().getToken();
		URL url = null;
		HttpURLConnection conn = null;
		try {
			url = new URL("https://www.googleapis.com/upload/youtube/v3/videos?uploadType=resumable&part=snippet,status");
			conn = httpClient.open(url);
			conn.setRequestMethod("POST");
			conn.setDoOutput(true);
			conn.setRequestProperty("Authorization", "Bearer "+acessToken);
			conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
			String metadata = """
					{
					  "snippet": {
					    "title": "%s",
					    "description": "%s"
					  },
					  "status": {
					    "privacyStatus": "public"
					  }
					}
					""".formatted(safe(post.getCaption()), safe(post.getCaption()));
			byte[] body = metadata.getBytes(StandardCharsets.UTF_8);
			conn.setRequestProperty("Content-Length", String.valueOf(body.length));
			conn.getOutputStream().write(body);
			int status = conn.getResponseCode();
			if (status >= 400) {
				throw new IllegalStateException("YouTube session creation failed: " + status);
			}
			String uploadUrl = conn.getHeaderField("Location");
	        if (uploadUrl == null) {
	            throw new IllegalStateException("Missing YouTube upload URL");
	        }
	        return uploadUrl;
		}catch(Exception e) {
			
		}

		String uploadUrl = conn.getHeaderField("Location");
		if (uploadUrl == null) {
			throw new IllegalStateException("Missing YouTube upload URL");
		}

		return uploadUrl;
	}
	
	private void uploadVideo(String uploadUrl, ScheduledPostTO post) {
		try {
			HttpURLConnection uploadConn = httpClient.open(new URL(uploadUrl));
			    uploadConn.setRequestMethod("PUT");
		        uploadConn.setDoOutput(true);
		        uploadConn.setRequestProperty("Content-Type", "video/*");
		        Path videoPath = Paths.get(post.getVideoUrl());

		        try (InputStream in = Files.newInputStream(videoPath)) {
		            StreamUtils.copy(in, uploadConn.getOutputStream());
		        }
		        int status = uploadConn.getResponseCode();
		        if (status >= 400) {
		            throw new IllegalStateException("YouTube upload failed: " + status);
		        }
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	private String safe(String value) {
        return value == null ? "" : value.replace("\"", "'");
    }
}
