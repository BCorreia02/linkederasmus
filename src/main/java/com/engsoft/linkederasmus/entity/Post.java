package com.engsoft.linkederasmus.entity;

import java.util.ArrayList;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "post")

public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(nullable = false, unique = true)
    private int idPost;

    @Column
    private String title;

    @Column
    private String lastTimeEdited;

    @Column(columnDefinition = "TEXT")
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idUser", referencedColumnName = "idUser")
    private User user;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idUni", referencedColumnName = "idUni")
    private University university;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments = new ArrayList<>();

    public void setLastTimeEdited(String str) {
        this.lastTimeEdited = str;
    }

    public int getPostId() {
        return idPost;
    }

}