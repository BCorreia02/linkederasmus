package com.engsoft.linkederasmus.service;

import com.engsoft.linkederasmus.dto.CommentDto;
import com.engsoft.linkederasmus.entity.Comment;
import com.engsoft.linkederasmus.entity.Post;
import com.engsoft.linkederasmus.entity.User;
import com.engsoft.linkederasmus.repository.CommentRepository;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class CommentServiceImpl implements CommentService {
    
    private CommentRepository commentRepository;

    @Autowired
    public CommentServiceImpl (CommentRepository commentRepository){
        this.commentRepository = commentRepository;
    }

    private CommentDto mapToCommentDto(Comment comment){
        CommentDto commentDto = new CommentDto();
        commentDto.setText(comment.getText());
        return commentDto;
    }

    @Override
    public List<CommentDto> getCommentsForPost(Post post){
        List<Comment> comments = commentRepository.findByPost(post);
        return comments.stream()
            .map(this::mapToCommentDto)
            .collect(Collectors.toList());
    }

    @Override
    public void createComment(String text, User user, Post post){
        Comment comment = new Comment();
        comment.setText(text);
        comment.setUser(user);
        comment.setPost(post);

        Date currentDate = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String currentDateTime = dateFormat.format(currentDate);
        comment.setLastTimeEdited(currentDateTime);

        commentRepository.save(comment);
    }

}

