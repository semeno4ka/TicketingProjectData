package com.cydeo.repository;

import com.cydeo.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

public interface ProjectRepository extends JpaRepository<Project, Long> {

    Project findByProjectCode(String code);
    @Transactional
    void deleteByProjectCode(String projectCode);
}
