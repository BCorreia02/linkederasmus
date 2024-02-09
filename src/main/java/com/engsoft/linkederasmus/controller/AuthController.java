package com.engsoft.linkederasmus.controller;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Collections;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import com.engsoft.linkederasmus.dto.UserDto;
import com.engsoft.linkederasmus.entity.Post;
import com.engsoft.linkederasmus.entity.University;
import com.engsoft.linkederasmus.entity.User;
import com.engsoft.linkederasmus.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.engsoft.linkederasmus.repository.PostRepository;
import com.engsoft.linkederasmus.repository.UniversityRepository;
import com.engsoft.linkederasmus.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;

@Controller
public class AuthController {

    private final UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UniversityRepository universityRepository;

    @Autowired
    private PostRepository postRepository;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    // handler method to handle home page request

    @GetMapping("/index")
    public String homePage(Authentication authentication, Model model, Principal principal, HttpSession session) {

        if (authentication != null && authentication.isAuthenticated()) {
            boolean isAdmin = authentication.getAuthorities().stream()
                    .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"));

            User loggedUser = userRepository.findByEmail(principal.getName());
            int userId = loggedUser.getIdUser();

            session.setAttribute("userId", userId);
            System.out.println(userId);

            model.addAttribute("loggedUserId", userId);

            List<User> users = userRepository.findAll();

            List<User> firstFiveResultsUser = new ArrayList<>();
            if (!users.isEmpty()) {
                int endIndex = Math.min(users.size(), 5);
                firstFiveResultsUser = users.subList(0, endIndex);
                model.addAttribute("users", firstFiveResultsUser);
            }

            List<University> universities = universityRepository.findAll();

            List<University> firstFiveResultsUni = new ArrayList<>();
            if (!universities.isEmpty()) {
                int endIndex = Math.min(universities.size(), 5);
                firstFiveResultsUni = universities.subList(0, endIndex);
                model.addAttribute("universities", firstFiveResultsUni);
            }

            if (isAdmin)
                return "logged-in-backend";

            return "logged-in-home";

        } else{

            List<Post> posts = postRepository.findAll();
            List<Post> firstFivePosts = new ArrayList<>();
            int endIndex = Math.max(posts.size(), 5);
            firstFivePosts = posts.subList(endIndex-5, posts.size());
            Collections.reverse(firstFivePosts);
            model.addAttribute("posts", firstFivePosts);
            return "index";
        }
    }

    @GetMapping("/logged-in-home")
    public String loggedInHome() {
        return "logged-in-home";
    }

    @GetMapping("/users")
    public String usersPage(Authentication authentication, Model model) {
        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"));
        if (authentication != null && authentication.isAuthenticated() && isAdmin) {
            List<UserDto> users = userService.findAllUsers();
            model.addAttribute("users", users);
            return "users";
        } else {
            return "login";
        }
    }

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        UserDto user = new UserDto();
        model.addAttribute("user", user);
        return "register";
    }

    // handler method to handle user registration form submit request
    @PostMapping("/register/save")
    public String registration(@Valid @ModelAttribute("user") UserDto userDto,
            BindingResult result,
            Model model) {

        User existingUser = userService.findUserByUsername(userDto.getUsername());

        if (existingUser != null && existingUser.getEmail() != null && !existingUser.getEmail().isEmpty()) {
            result.rejectValue("email", null,
                    "There is already an account registered with the same email");
        }

        if (result.hasErrors()) {
            model.addAttribute("user", userDto);
            return "/register";
        }

        userService.saveUser(userDto);
        return "redirect:/register?success";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

}