package com.engsoft.linkederasmus.controller;

import com.engsoft.linkederasmus.dto.CommentDto;
import com.engsoft.linkederasmus.entity.Post;
import com.engsoft.linkederasmus.entity.User;
import com.engsoft.linkederasmus.repository.CommentRepository;
import com.engsoft.linkederasmus.repository.UserRepository;
import com.engsoft.linkederasmus.service.CommentService;
import com.engsoft.linkederasmus.service.PostService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.security.core.userdetails.UserDetails;

import jakarta.validation.Valid;

@Controller
public class CommentController {

    private CommentService commentService;
    private UserRepository userRepository;
    private PostService postService;

    @Autowired
    private CommentRepository commentRepository;

    public CommentController(CommentService commentService, PostService postService, UserRepository userRepository) {
        this.commentService = commentService;
        this.userRepository = userRepository;
        this.postService = postService;
    }


@PostMapping("/comments/save")
public String saveComment(@Valid @ModelAttribute("commentDto") CommentDto commentDto,
                      BindingResult result,
                      Model model,
                      Authentication authentication) {

        System.out.println("que merda e esta");
  
        // Get the currently authenticated user
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String username = userDetails.getUsername();
        System.out.println(username);
        User user = userRepository.findByEmail(username);
        System.out.println(user.getUsername());
        
        // Retrieve the associated post for the comment
        int idPost = commentDto.getIdPost();
        System.out.println("tem id");
        System.out.println(idPost);
        Post post = postService.findPostById(idPost);

        if (post != null) {
            commentService.createComment(commentDto.getText(), user, post);
            return "redirect:/profile/" + post.getUser().getIdUser();
        } else {
            System.out.println("Post object is null");
        }
    
        return "login"; 
    }

    @PostMapping("/comments/delete")
    @Transactional
    public String deleteComment(@RequestParam("idComment") int idComment, 
                                @RequestParam("idUser") int idUser) {
        commentRepository.deleteByIdComment(idComment);
        return "redirect:/profile/"  + idUser;
    }

}




