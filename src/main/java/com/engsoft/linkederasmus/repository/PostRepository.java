package com.engsoft.linkederasmus.repository;

import com.engsoft.linkederasmus.entity.User;
import com.engsoft.linkederasmus.entity.Post;
import com.engsoft.linkederasmus.entity.University;
import com.engsoft.linkederasmus.dto.PostDto;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {

    List<PostDto> findByUser(String username);

    List<Post> findByUser(User user);

    List<Post> findByUniversity(University university);

    List<Post> findAllByUniversityId(int id);

    
    void deleteByIdPost(int idPost);

}