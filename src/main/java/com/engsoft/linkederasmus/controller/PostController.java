package com.engsoft.linkederasmus.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.transaction.annotation.Transactional;

import com.engsoft.linkederasmus.dto.PostDto;
import com.engsoft.linkederasmus.dto.UniversityDto;
import com.engsoft.linkederasmus.entity.Post;
import com.engsoft.linkederasmus.repository.PostRepository;
import com.engsoft.linkederasmus.service.PostService;
import com.engsoft.linkederasmus.service.UniversityService;
import com.engsoft.linkederasmus.service.UserService;

import jakarta.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Controller
public class PostController {

    private UniversityService universityService;
    private PostService postService;

    @Autowired
    PostRepository postRepository;

    public PostController(UniversityService universityService, PostService postService, UserService userService) {
        this.universityService = universityService;
        this.postService = postService;
    }

    @GetMapping("/createPost")
    public String newPost(Model model, Authentication authentication) {
        if (authentication != null && authentication.isAuthenticated()) {
            List<UniversityDto> universities = universityService.findAllUniversities();
            PostDto post = new PostDto();
            model.addAttribute("post", post);
            model.addAttribute("universities", universities);
            return "createPost";
        } else {
            return "login";
        }
    }

    @PostMapping("/createPost/save")
    public String registration(@Valid @ModelAttribute("post") PostDto postDto,
            BindingResult result,
            Model model) {

        postService.savePost(postDto);
        return "redirect:/index";
    }

    @PostMapping("/posts/edit")
    public String editPost(
        @RequestParam("idPost") int idPost,
        @RequestParam("idUser") int idUser,
        @RequestParam("updatedTitle") String updatedTitle,
        @RequestParam("updatedContent") String updatedContent
        ) {
        // Retrieve the post from the database using the postId
        Post post = postService.findPostById(idPost);
        System.out.println("entrei");
        System.out.println(idUser);
        
        // Update the post's title and content
        post.setTitle(updatedTitle);
        post.setContent(updatedContent);

        Date currentDate = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String currentDateTime = dateFormat.format(currentDate);
        post.setLastTimeEdited(currentDateTime);

        
        // Save the updated post to the database
        postRepository.save(post);
        
        // Redirect to the user's profile page or any other appropriate page
        return "redirect:/profile/"  + idUser;
    }

    @PostMapping("/posts/delete")
    @Transactional
    public String deletePost(@RequestParam("idPost") int idPost,
                            @RequestParam("idUser") int idUser) {
        System.out.println("aqui" + idPost);
        postRepository.deleteByIdPost(idPost);
        return "redirect:/profile/"  + idUser;// Redirect to the profile page after deletion
    }

}
