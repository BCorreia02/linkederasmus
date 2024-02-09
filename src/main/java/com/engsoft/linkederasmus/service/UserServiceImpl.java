package com.engsoft.linkederasmus.service;

import com.engsoft.linkederasmus.dto.UserDto;
import com.engsoft.linkederasmus.entity.Role;
import com.engsoft.linkederasmus.entity.User;
import com.engsoft.linkederasmus.repository.RoleRepository;
import com.engsoft.linkederasmus.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository,
            RoleRepository roleRepository,
            PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void saveUser(UserDto userDto) {

        User user = new User();
        user.setName(userDto.getUsername());
        user.setEmail(userDto.getEmail());
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        user.setIdUser(userRepository.findAll().size() + 1);

        Role role = roleRepository.findByName("ROLE_USER");

        user.getRoles().add(role);

        userRepository.save(user);
    }

    @Override
    public User findUserByUsername(String name) {
        Iterable<User> users = userRepository.findAll();
        for (User user : users) {
            if (user.getUsername().equals(name))
                return user;
        }
        return null;
    }

    @Override
    public List<UserDto> findAllUsers() {
        List<User> users = userRepository.findAll();
        return users.stream()
                .map((user) -> mapToUserDto(user))
                .collect(Collectors.toList());
    }

    private UserDto mapToUserDto(User user) {
        UserDto userDto = new UserDto();
        userDto.setUsername(userDto.getUsername());
        userDto.setEmail(user.getEmail());
        return userDto;
    }

}