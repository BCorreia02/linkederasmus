package com.engsoft.linkederasmus.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class UniversityDto {

    @NotEmpty
    private int idUni;
    @NotEmpty
    private String name;

}