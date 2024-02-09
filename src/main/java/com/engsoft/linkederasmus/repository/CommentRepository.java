package com.engsoft.linkederasmus.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.engsoft.linkederasmus.entity.Comment;
import com.engsoft.linkederasmus.entity.Post;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    List<Comment> findByPost(Post post);

    void deleteByIdComment(int idComment);
}
