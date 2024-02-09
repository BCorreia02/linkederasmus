package com.engsoft.linkederasmus.repository;

import com.engsoft.linkederasmus.entity.University;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.List;

public interface UniversityRepository extends JpaRepository<University, Integer> {

    University findUniversityById(int idUni);

    Optional<University> findByIdUni(int idUni);

    List<University> findUniversitiesByNameContainingIgnoreCase(String name);

    List<University> findAll();

}
