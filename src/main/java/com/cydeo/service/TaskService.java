package com.cydeo.service;

import com.cydeo.dto.ProjectDTO;
import com.cydeo.dto.TaskDTO;
import com.cydeo.dto.UserDTO;
import com.cydeo.entity.Task;
import com.cydeo.entity.User;
import com.cydeo.enums.Status;

import java.util.List;

public interface TaskService {

    void save(TaskDTO task);
    void update(TaskDTO task);
    void deleteById(Long id);
    List<TaskDTO> listAllTasks();
    TaskDTO findTaskById(Long id);
    Integer totalNonCompletedTask(String projectCode);
    Integer totalCompletedTask(String projectCode);
    void deleteByProject(ProjectDTO projectDTO);
    void completeByProject(ProjectDTO dto);
    List<TaskDTO> listAllTasksByStatusIsNot(Status status);
    List<TaskDTO>listAllTasksByStatus(Status status);
    List<TaskDTO> listAllNonCompletedByAssignedEmployee(UserDTO assignedEmployee);

}
