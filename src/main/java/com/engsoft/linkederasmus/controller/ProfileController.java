package com.engsoft.linkederasmus.controller;

import java.util.List;
import com.engsoft.linkederasmus.entity.User;
import com.engsoft.linkederasmus.entity.University;
import com.engsoft.linkederasmus.entity.Post;
import org.springframework.stereotype.Controller;
import com.engsoft.linkederasmus.repository.UserRepository;

import jakarta.servlet.http.HttpSession;

import com.engsoft.linkederasmus.repository.PostRepository;
import com.engsoft.linkederasmus.repository.UniversityRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

@Controller
public class ProfileController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UniversityRepository universityRepository;

    @Autowired
    private PostRepository postRepository;

    @GetMapping("/profile/{idUser}")
    public String getUserProfile(@PathVariable int idUser, Model model, HttpSession session) {

        Optional<User> optionalUser = userRepository.findByIdUser(idUser);

        if (optionalUser.isPresent()) {

            User user = optionalUser.get();

            List<Post> posts = postRepository.findByUser(user);

            model.addAttribute("user", user);
            model.addAttribute("posts", posts);

            int userId = (int) session.getAttribute("userId");

            model.addAttribute("loggedUserId", userId);

            if (userId == idUser) {
                return "user-profile-owner";
            }

            return "user-profile-view";
        }

        return "index";
    }

    @GetMapping("/search")
    public String searchUsers(@RequestParam("query") String query, Model model, HttpSession session) {

        List<User> users = userRepository.findUsersByUsernameContainingIgnoreCase(query);
        List<University> universities = universityRepository.findUniversitiesByNameContainingIgnoreCase(query);

        model.addAttribute("universities", universities);
        model.addAttribute("users", users);

        int userId = (int) session.getAttribute("userId");

        model.addAttribute("loggedUserId", userId);

        return "logged-in-home";
    }

}