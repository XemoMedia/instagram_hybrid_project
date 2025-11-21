package com.example.instagram.service;

import com.example.instagram.dto.InstagramCommentDTO;
import com.example.instagram.model.InstagramComment;
import com.example.instagram.repository.InstagramCommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class InstagramCommentService {

    @Autowired
    private InstagramCommentRepository instagramCommentRepository;

    public List<InstagramCommentDTO> getCommentsByFromId(String fromId) {
        return instagramCommentRepository.findByFromId(fromId).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public List<InstagramCommentDTO> getCommentsByFromUsername(String fromUsername) {
        return instagramCommentRepository.findByFromUsername(fromUsername).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
    
    public List<InstagramCommentDTO> findAll() {
        return instagramCommentRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public List<InstagramCommentDTO> getCommentsByMediaId(String mediaId) {
        return instagramCommentRepository.findByMedia_Id(mediaId).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    private InstagramCommentDTO convertToDto(InstagramComment comment) {
        return new InstagramCommentDTO(
                comment.getId(),
                comment.getText(),
                comment.getTimestamp(),
                comment.getFromId(),
                comment.getFromUsername(),
                comment.getMedia() != null ? comment.getMedia().getId() : null
                );
        
    }
}
