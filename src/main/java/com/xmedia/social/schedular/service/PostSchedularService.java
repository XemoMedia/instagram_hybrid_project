package com.xmedia.social.schedular.service;

import java.io.File;
import java.nio.file.Files;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.xmedia.social.schedular.dispatcher.SocialPostDispatcher;
import com.xmedia.social.schedular.enm.SocialPlatformEnum;
import com.xmedia.social.schedular.entity.ScheduledPostEntity;
import com.xmedia.social.schedular.repo.PostSchedularRepository;
import com.xmedia.social.schedular.to.ScheduledPostTO;
import com.xmedia.social.schedular.to.SocialDetailsTO;

@Service
public class PostSchedularService {
	
	@Autowired
	PostSchedularRepository repo;
	
	@Autowired
	ObjectMapper objectMapper;
	
	@Autowired
	SocialPostDispatcher socialPostDispatcher;

	public ScheduledPostTO postSchedular(String caption, List<String> platforms, String scheduledTime, MultipartFile image,
			MultipartFile video, String acessToken) {
		ScheduledPostTO scheduledPostTO = setSchedulePost(caption, platforms, scheduledTime, image, video, acessToken);
		ScheduledPostEntity scheduledPostEntity = objectMapper.convertValue(scheduledPostTO, ScheduledPostEntity.class);
		ScheduledPostTO  sPT = objectMapper.convertValue(repo.save(scheduledPostEntity), ScheduledPostTO.class);
		//implementation to post Social Media
		socialPostDispatcher.dispatch(sPT);
		return sPT;
		
	}

	private ScheduledPostTO setSchedulePost(String caption, List<String> platforms, String scheduledTime,
			MultipartFile image, MultipartFile video, String acessToken) {
		ScheduledPostTO scheduledPostTO = new ScheduledPostTO();
		scheduledPostTO.setCaption(caption);
		scheduledPostTO.setStatus("SCHEDULED");
		
		 List<SocialDetailsTO> socialDetailsList = new ArrayList<>();
		 for (String platformStr : platforms) {
			 SocialDetailsTO sd = new SocialDetailsTO();
	            sd.setPlatform(SocialPlatformEnum.valueOf(platformStr.toUpperCase()));
	            sd.setEnabled(true); // default true
	            sd.setStatus("INPROGRESS");
	            sd.setToken(acessToken);
	            socialDetailsList.add(sd);
	        }
		scheduledPostTO.setCaption(caption);
		scheduledPostTO.setPlatforms(socialDetailsList);
		scheduledPostTO.setStatus("SCHEDULED");
		//DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		//scheduledPostTO.setScheduledTime(LocalDateTime.parse(scheduledTime, fmt));
		scheduledPostTO.setScheduledTime(
			    LocalDateTime.parse(scheduledTime, DateTimeFormatter.ISO_LOCAL_DATE_TIME)
			);
		try {
			if (image != null && !image.isEmpty()) {
				File f = new File("uploads/" + UUID.randomUUID() + "_" + image.getOriginalFilename());
				f.getParentFile().mkdirs();
				Files.write(f.toPath(), image.getBytes());
				scheduledPostTO.setImageUrl("/uploads/" + f.getName());
			}
			if (video != null && !video.isEmpty()) {
				File f = new File("uploads/" + UUID.randomUUID() + "_" + video.getOriginalFilename());
				f.getParentFile().mkdirs();
				Files.write(f.toPath(), video.getBytes());
				scheduledPostTO.setVideoUrl(f.getAbsolutePath());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return scheduledPostTO;
	}

}
