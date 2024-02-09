package com.engsoft.linkederasmus.service;

import java.util.List;

import com.engsoft.linkederasmus.dto.UniversityDto;
import com.engsoft.linkederasmus.entity.University;
import com.engsoft.linkederasmus.entity.User;

public interface UniversityService {
    List<UniversityDto> findAllUniversities();
    University findUniversitybyId(int id);
    void saveUniversity(UniversityDto uni);
    University findUniversityByName(String name);
    UniversityDto toDto(University university);
    List<University> findAllByUser(User user);
}
