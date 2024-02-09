package com.engsoft.linkederasmus.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableWebSecurity
@EnableTransactionManagement
public class SpringSecurity {

        @Autowired
        private UserDetailsService userDetailsService;

        @Bean
        public static PasswordEncoder passwordEncoder() {
                return new BCryptPasswordEncoder();
        }

        @Bean
        public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
                http.csrf().disable()
                                .authorizeHttpRequests(
                                                (authorize) -> authorize
                                                                .requestMatchers("/createPost/**").authenticated()
                                                                .requestMatchers("/register/**").permitAll()
                                                                .requestMatchers("/logged-in-home").authenticated()
                                                                .requestMatchers("/index").permitAll()
                                                                .requestMatchers("/users").hasRole("ADMIN")
                                                                .requestMatchers("/logged-in-backend").hasRole("ADMIN")
                                                                .requestMatchers("/profile/{userId}").authenticated()
                                                                .requestMatchers("/user-profile-owner").authenticated()
                                                                .requestMatchers("/user-profile-view").authenticated()
                                                                .requestMatchers("/search").authenticated()
                                                                .requestMatchers("/universities").authenticated()
                                                                .requestMatchers("/university/{idUni}").authenticated()
                                                                .requestMatchers("/university-profile-owner")
                                                                .authenticated()
                                                                .requestMatchers("/university-profile-view")
                                                                .authenticated()
                                                                .requestMatchers("/addUniversity")
                                                                .authenticated()
                                                                .requestMatchers("/posts/edit").authenticated()
                                                                .requestMatchers("/comments/save").authenticated()
                                                                .requestMatchers("/comments/delete").authenticated()
                                                                .requestMatchers("/posts/delete").authenticated()
                                                                )
                                .formLogin(
                                                form -> form
                                                                .loginPage("/login")
                                                                .loginProcessingUrl("/login")
                                                                .defaultSuccessUrl("/index")
                                                                .permitAll())
                                .logout(
                                                logout -> logout
                                                                .logoutSuccessUrl("/login")
                                                                .logoutRequestMatcher(
                                                                                new AntPathRequestMatcher("/logout"))
                                                                .permitAll());

                return http.build();
        }

        @Autowired
        public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
                auth
                                .userDetailsService(userDetailsService)
                                .passwordEncoder(passwordEncoder());
        }
}