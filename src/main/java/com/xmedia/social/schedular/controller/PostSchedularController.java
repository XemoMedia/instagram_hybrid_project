package com.xmedia.social.schedular.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.xmedia.social.schedular.service.PostSchedularService;
import com.xmedia.social.schedular.to.ScheduledPostTO;
@RestController
@RequestMapping("/api/posts")
public class PostSchedularController {
	
	@Autowired
	PostSchedularService postSchedularService;
	
	@PostMapping(consumes="multipart/form-data")
	 public ScheduledPostTO create(
	  @RequestParam String caption,
	  @RequestParam List<String> platforms,
	  @RequestParam String scheduledTime,
	  @RequestPart(required=false) MultipartFile image,
	  @RequestPart(required=false) MultipartFile video,
	  @RequestParam String acessToken
	 ) throws Exception {
		return postSchedularService.postSchedular(caption,platforms,scheduledTime,image,video, acessToken);
	 }

}
