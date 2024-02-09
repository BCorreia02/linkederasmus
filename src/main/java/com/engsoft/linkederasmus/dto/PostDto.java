package com.engsoft.linkederasmus.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import jakarta.validation.constraints.NotEmpty;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class PostDto {

    private int idPost;
    @NotEmpty
    private String content;
    @NotEmpty
    private String title;
    private String lastTimeEdited;
    private int IdUni;
}