package com.example.instagram.service;

import com.example.instagram.entity.InstaComment;
import com.example.instagram.entity.InstagramResponseDto;
import com.example.instagram.repository.InstaCommentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class InstagramCommentService {

    private final InstaCommentRepository repo;

    public InstagramCommentService(InstaCommentRepository repo) {
        this.repo = repo;
    }

    @Transactional
    public void saveCommentsFromInstagramResponse(InstagramResponseDto responseDto) {
        if (responseDto == null || responseDto.getData() == null) {
            return;
        }
        responseDto.getData().forEach(dataItem -> {
            String commentId = dataItem.getId();
            String text = dataItem.getText();
            String timestamp = dataItem.getTimestamp();
            String userId = dataItem.getFrom() != null ? dataItem.getFrom().getId() : null;
            String mediaId = "NOT_AVAILABLE"; // Media ID is not directly in the provided response structure

            saveComment(commentId, mediaId, text, userId, timestamp);
        });
    }

    @Transactional
    public void saveComment(String commentId, String mediaId, String text,
                            String userId, String timestamp) {

        if (commentId == null || commentId.isBlank()) {
            return;
        }

        if (repo.existsById(commentId)) return;

        InstaComment c = new InstaComment();
        c.setId(commentId);
        c.setMediaId(mediaId);
        c.setText(text);
        c.setUserId(userId);
       // c.setUsername(username);
        c.setTimestamp(timestamp);

        repo.save(c);
    }
}
