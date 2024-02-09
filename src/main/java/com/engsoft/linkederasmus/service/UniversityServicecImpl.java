package com.engsoft.linkederasmus.service;

import com.engsoft.linkederasmus.entity.University;
import com.engsoft.linkederasmus.entity.User;
import com.engsoft.linkederasmus.dto.UniversityDto;
import com.engsoft.linkederasmus.repository.PostRepository;
import com.engsoft.linkederasmus.repository.UniversityRepository;
import com.engsoft.linkederasmus.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class UniversityServicecImpl implements UniversityService {

    @Autowired
    private ModelMapper modelMapper;

    private UniversityRepository UniversityRepository;
    private PostRepository postRepository;
    private UserRepository userRepository;

    public UniversityServicecImpl(UniversityRepository universityRepository, PostRepository postRepository,
            UserRepository userRepository) {
        this.UniversityRepository = universityRepository;
        this.postRepository = postRepository;
        this.userRepository = userRepository;
    }

    @Override
    public List<UniversityDto> findAllUniversities() {
        List<University> universitys = UniversityRepository.findAll();
        return universitys.stream()
                .map((university) -> mapToUniversityDto(university))
                .collect(Collectors.toList());
    }

    private UniversityDto mapToUniversityDto(University university) {
        UniversityDto uniDto = new UniversityDto();
        uniDto.setName(university.getName());
        uniDto.setIdUni(university.getIdUni());
        return uniDto;
    }

    public University findUniversitybyId(int id) {
        List<University> unis = UniversityRepository.findAll();
        for (University i : unis) {
            if (i.getIdUni() == id)
                return i;
        }
        return null;
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
    public void saveUniversity(UniversityDto universityDto) {

        University university = new University();
        university.setName(universityDto.getName());
        university.setIdUni(postRepository.findAll().size() + 1);
        university.setPosts(null);
        String username = getCurrentUserName();
        User user = userRepository.findByEmail(username);
        university.setUser(user);
        UniversityRepository.save(university);
    }

    @Override
    public University findUniversityByName(String name) {
        List<University> unis = UniversityRepository.findAll();
        for (University i : unis) {
            if (i.getName() == name)
                return i;
        }
        return null;
    }

    public UniversityDto toDto(University university) {
        UniversityDto universityDto = new UniversityDto();
        universityDto = modelMapper.map(university, UniversityDto.class);
        return universityDto;
    }

    @Override
    public List<University> findAllByUser(User user) {
        List<University> unis = UniversityRepository.findAll();
        List<University> filtered = new ArrayList<University>();
        if (!unis.isEmpty()) {
            for (University i : unis) {
                if (i.getUser() == user)
                    filtered.add(i);
            }
            return filtered;
        } else {
            return null;
        }
    }

}
