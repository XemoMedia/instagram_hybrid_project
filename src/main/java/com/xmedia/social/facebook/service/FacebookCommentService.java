package com.xmedia.social.facebook.service;

import com.xmedia.social.feign.client.FacebookGraphClient;
import com.xmedia.social.facebook.dto.FacebookCommentResponse;
import com.xmedia.social.facebook.entity.FacebookComment;
import com.xmedia.social.facebook.repository.FacebookCommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FacebookCommentService {

    @Autowired
    private FacebookCommentRepository repository;

    @Autowired
    private FacebookGraphClient facebookGraphClient;

    @Value("${facebook.graph.api.access-token}")
    private String accessToken;

    public void fetchAndSaveComments(String postId) {
        FacebookCommentResponse response = facebookGraphClient.getPostComments(postId,
                "id,message,from,created_time", accessToken);

        if (response != null && response.getData() != null) {
            List<FacebookComment> comments = response.getData().stream().map(commentData -> {
                FacebookComment comment = new FacebookComment();
                comment.setCommentId(commentData.getId());
                comment.setPostId(postId);
                comment.setMessage(commentData.getMessage());
                comment.setFromName(commentData.getFrom().getName());
                comment.setFromId(commentData.getFrom().getId());
                comment.setCreatedTime(commentData.getCreated_time());
                return comment;
            }).toList();

            repository.saveAll(comments);
        }
    }
}
