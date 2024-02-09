package com.engsoft.linkederasmus.service;

import com.engsoft.linkederasmus.dto.PostDto;
import com.engsoft.linkederasmus.entity.User;
import com.engsoft.linkederasmus.entity.Post;

import java.util.List;

public interface PostService {

    User findUserbyId(int idUser);

    List<PostDto> findByUser(String username);

    List<PostDto> findAllPosts();

    void savePost(PostDto postDto);

    Post findPostById(int idPost);

    List<PostDto> findByUniversity(String name);

    List<PostDto> findByMostRecent(String name);
}