package com.engsoft.linkederasmus.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;
import java.util.Optional;

import com.engsoft.linkederasmus.entity.Post;
import com.engsoft.linkederasmus.entity.University;
import com.engsoft.linkederasmus.entity.User;
import com.engsoft.linkederasmus.repository.UniversityRepository;
import com.engsoft.linkederasmus.repository.UserRepository;
import com.engsoft.linkederasmus.service.UniversityService;
import com.engsoft.linkederasmus.service.UserService;
import com.engsoft.linkederasmus.repository.PostRepository;

import jakarta.servlet.http.HttpSession;

@Controller
public class UniversityController {

    private UniversityService universityService;

    @Autowired
    private UniversityRepository universityRepository;

    @Autowired
    private UserRepository userRepository;

    private PostRepository postRepository;

    public UniversityController(UniversityService universityService, UserService userService,
            PostRepository postRepository) {
        this.universityService = universityService;
        this.postRepository = postRepository;
    }

    @GetMapping("/universities")
    public String getUniversityList(Model model, Authentication authentication) {
        System.out.println("1.2");

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        User user = userRepository.findByEmail(userDetails.getUsername());

        List<University> universities = universityService.findAllByUser(user);

        int loggedUserId = user.getIdUser();
        model.addAttribute("loggedUserId", loggedUserId);
        model.addAttribute("user", user);
        model.addAttribute("universities", universities);
        return "universityList";
    }

    @GetMapping("/university/{idUni}")
    public String getUserProfile(@PathVariable int idUni, Model model, HttpSession session) {
        System.out.println("0");

        Optional<University> optionalUni = universityRepository.findById(idUni);

        if (optionalUni.isPresent()) {
            System.out.println("1");

            University uni = optionalUni.get();

            List<Post> posts = postRepository.findByUniversity(uni);

            model.addAttribute("university", uni);
            model.addAttribute("posts", posts);

            int userId = (int) session.getAttribute("userId");

            model.addAttribute("loggedUserId", userId);

            if (userId == uni.getUser().getIdUser()) {
                return "university-profile-owner";
            }

            return "university-profile-view";
        }

        return "index";
    }

    // Mapping for displaying the add university form
    @GetMapping("/addUniversity")
    public String showAddUniversityForm(Model model) {
        model.addAttribute("university", new University());
        return "addUniversity";
    }

    @PostMapping("/addUniversity")
    public String addUniversity(@ModelAttribute("universityName") String universityName,
            Authentication authentication) {
        University university = new University();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        User creator = userRepository.findByEmail(userDetails.getUsername());
        university.setUser(creator);
        university.setName(universityName);
        universityRepository.save(university);

        return "redirect:/universities";
    }
}
