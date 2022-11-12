package com.cydeo.service;

import com.cydeo.dto.TaskDTO;
import com.cydeo.entity.Task;

import java.util.List;

public interface TaskService {

    void save(TaskDTO task);
    void update(TaskDTO task);
    void deleteById(Long id);
    List<TaskDTO> listAllTasks();
    TaskDTO findTaskById(Long id);
}
