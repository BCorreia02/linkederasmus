package com.engsoft.linkederasmus.service;

import java.util.List;

import com.engsoft.linkederasmus.dto.CommentDto;
import com.engsoft.linkederasmus.entity.Post;
import com.engsoft.linkederasmus.entity.User;

public interface CommentService{

    List<CommentDto> getCommentsForPost(Post post);

    void createComment(String text, User user, Post post);
    
}

    