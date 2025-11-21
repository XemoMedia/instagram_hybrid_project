package com.example.instagram.dto;

import java.util.List;

public class Comments {
    private List<CommentData> data;

    // Getter and Setter
    public List<CommentData> getData() {
        return data;
    }

    public void setData(List<CommentData> data) {
        this.data = data;
    }
}
