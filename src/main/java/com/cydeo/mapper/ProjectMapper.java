package com.cydeo.mapper;

import com.cydeo.dto.ProjectDTO;
import com.cydeo.dto.UserDTO;
import com.cydeo.entity.Project;
import com.cydeo.entity.User;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import javax.persistence.Column;

@Component
public class ProjectMapper {
    private final ModelMapper modelMapper;

    public ProjectMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public ProjectDTO convertToDTO(Project entity){
        return modelMapper.map(entity, ProjectDTO.class);
    }
    public Project convertToEntity(ProjectDTO dto){
        return modelMapper.map(dto, Project.class);
    }
}

