package com.engsoft.linkederasmus.service;

import com.engsoft.linkederasmus.dto.PostDto;
import com.engsoft.linkederasmus.entity.Post;
import com.engsoft.linkederasmus.entity.University;
import com.engsoft.linkederasmus.entity.User;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import java.util.Date;
import java.text.SimpleDateFormat;

import com.engsoft.linkederasmus.repository.PostRepository;
import com.engsoft.linkederasmus.repository.UniversityRepository;
import com.engsoft.linkederasmus.repository.UserRepository;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

@Service
public class PostServiceImpl implements PostService {

    private UniversityRepository universityRepository;
    private PostRepository postRepository;
    private UserRepository userRepository;

    public PostServiceImpl(UserRepository userRepository,
            PostRepository postRepository, UniversityRepository universityRepository) {

        this.universityRepository = universityRepository;
        this.postRepository = postRepository;
        this.userRepository = userRepository;
    }

    private PostDto mapToPostDto(Post post) {
        PostDto postDto = new PostDto();
        postDto.setContent(post.getContent());
        postDto.setTitle(post.getTitle());
        return postDto;
    }

    @Override
    public List<PostDto> findAllPosts() {
        List<Post> posts = postRepository.findAll();
        return posts.stream()
                .map((post) -> mapToPostDto(post))
                .collect(Collectors.toList());
    }

    @Override
    public User findUserbyId(int id) {
        List<User> users = userRepository.findAll();
        for (User i : users) {
            if (i.getIdUser() == id)
                return i;
        }
        return null;
    }

    @Override
    public List<PostDto> findByUser(String username) {

        List<Post> posts = postRepository.findAll();
        List<Post> filtered = new ArrayList<>();
        for (Post p : posts) {
            if (p.getUser().getUsername().equals(username)) {
                filtered.add(p);
            }
        }

        return filtered.stream()
                .map((post) -> mapToPostDto(post))
                .collect(Collectors.toList());
    }

    @Override
    public List<PostDto> findByUniversity(String name) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'findByUniversity'");
    }

    @Override
    public List<PostDto> findByMostRecent(String name) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'findByMostRecent'");
    }

    public String getCurrentUserName() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return null;
        }

        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        String username = "";
        if (principal instanceof UserDetails) {
            username = ((UserDetails) principal).getUsername();
        } else {
            username = principal.toString();
        }

        return username;

    }

    @Override
    public void savePost(PostDto postDto) {

        Date currentDate = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String currentDateTime = dateFormat.format(currentDate);

        Post post = new Post();
        post.setTitle(postDto.getTitle());
        post.setIdPost(postRepository.findAll().size() + 1);
        post.setContent(postDto.getContent());

        String username = getCurrentUserName();
        System.out.println(username);
        User user = userRepository.findByEmail(username);
        post.setUser(user);

        University university = universityRepository.findUniversityById(postDto.getIdUni());
        System.out.println("unis");
        System.out.println(postDto.getIdUni());
        System.out.println(university);
        post.setUniversity(university);
        System.out.println(post.getUniversity());
        post.setLastTimeEdited(currentDateTime);

        postRepository.save(post);
    }

    @Override
    public Post findPostById(int postId) {
    List<Post> posts = postRepository.findAll();
    for (Post post : posts) {
        if (post.getPostId() == postId) {
            return post;
        }
    }
    return null;
}
}
