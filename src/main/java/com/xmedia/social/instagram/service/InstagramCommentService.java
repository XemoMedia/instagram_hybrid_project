package com.xmedia.social.instagram.service;

import com.xmedia.social.instagram.dto.InstagramCommentDto;
import com.xmedia.social.instagram.entity.InstagramComment;
import com.xmedia.social.instagram.repository.InstagramCommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class InstagramCommentService {

    @Autowired
    private InstagramCommentRepository instagramCommentRepository;

    public List<InstagramCommentDto> getCommentsByFromId(String fromId) {
        return instagramCommentRepository.findByFromId(fromId).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public List<InstagramCommentDto> getCommentsByFromUsername(String fromUsername) {
        return instagramCommentRepository.findByFromUsername(fromUsername).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
    
    public List<InstagramCommentDto> findAll() {
        return instagramCommentRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public List<InstagramCommentDto> getCommentsByMediaId(String mediaId) {
        return instagramCommentRepository.findByMedia_Id(mediaId).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    private InstagramCommentDto convertToDto(InstagramComment comment) {
        return new InstagramCommentDto(
                comment.getId(),
                comment.getText(),
                comment.getTimestamp(),
                comment.getFromId(),
                comment.getFromUsername(),
                comment.getMedia() != null ? comment.getMedia().getId() : null
                );
        
    }
}

