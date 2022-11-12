package com.cydeo.service.impl;

import com.cydeo.dto.TaskDTO;
import com.cydeo.entity.Task;
import com.cydeo.enums.Status;
import com.cydeo.mapper.TaskMapper;
import com.cydeo.repository.TaskRepository;
import com.cydeo.service.TaskService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;
    private final TaskMapper taskMapper;

    public TaskServiceImpl(TaskRepository taskRepository, TaskMapper taskMapper) {
        this.taskRepository = taskRepository;
        this.taskMapper = taskMapper;
    }

    @Override
    public void save(TaskDTO task) {
        task.setTaskStatus(Status.OPEN);
        task.setAssignedDate(LocalDate.now());
        Task taskNew = taskMapper.convertToEntity(task);
        taskRepository.save(taskNew);
    }

    @Override
    public void update(TaskDTO dto) {
        Optional<Task> task=taskRepository.findById(dto.getId());
        Task convertedTask=taskMapper.convertToEntity(dto);
        if(task.isPresent()){
            convertedTask.setTaskStatus(task.get().getTaskStatus());
            convertedTask.setAssignedDate(task.get().getAssignedDate());
            taskRepository.save(convertedTask);
        }
    }

    @Override
    public void deleteById(Long id) {
        Optional<Task> foundTask = taskRepository.findById(id);

        if(foundTask.isPresent()){// handle exceptions
            foundTask.get().setIsDeleted(true);
            taskRepository.save(foundTask.get());
        }
    }

    @Override
    public List<TaskDTO> listAllTasks() {
        return taskRepository.findAll().stream().map(taskMapper::convertToDTO).collect(Collectors.toList());
    }

    @Override
    public TaskDTO findTaskById(Long id) {
        Optional<Task> task1=taskRepository.findById(id);
        if(task1.isPresent()){
            return taskMapper.convertToDTO(task1.get());
        }
        return null;
    }
}
