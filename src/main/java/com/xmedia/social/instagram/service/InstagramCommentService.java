package com.xmedia.social.instagram.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xmedia.social.instagram.dto.InstagramCommentDto;
import com.xmedia.social.instagram.entity.Comment;
import com.xmedia.social.instagram.repository.CommentRepository;

@Service
public class InstagramCommentService {

    @Autowired
    private CommentRepository commentRepository;

    public List<InstagramCommentDto> getCommentsByFromId(String fromId) {
        return commentRepository.findByAccountId(fromId).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public List<InstagramCommentDto> getCommentsByFromUsername(String fromUsername) {
            return commentRepository.findByUsername(fromUsername).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
    
    public List<InstagramCommentDto> findAll() {
        return commentRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public List<InstagramCommentDto> getCommentsByMediaId(String mediaId) {
        return commentRepository.findByMedia_Id(mediaId).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    private InstagramCommentDto convertToDto(Comment comment) {
        return new InstagramCommentDto(
                comment.getId(),
                comment.getText(),
                comment.getTimestamp(),
                comment.getAccountId(),
                comment.getUsername(),
                comment.getMedia() != null ? comment.getMedia().getId() : null
                );
        
    }
}

