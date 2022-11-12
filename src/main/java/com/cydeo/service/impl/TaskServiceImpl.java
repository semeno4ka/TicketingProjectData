package com.cydeo.service.impl;

import com.cydeo.dto.ProjectDTO;
import com.cydeo.dto.TaskDTO;
import com.cydeo.dto.UserDTO;
import com.cydeo.entity.Project;
import com.cydeo.entity.Task;
import com.cydeo.enums.Status;
import com.cydeo.mapper.ProjectMapper;
import com.cydeo.mapper.TaskMapper;
import com.cydeo.mapper.UserMapper;
import com.cydeo.repository.TaskRepository;
import com.cydeo.service.TaskService;
import com.cydeo.service.UserService;
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
    private final ProjectMapper projectMapper;
    private final UserService userService;
    private final UserMapper userMapper;

    public TaskServiceImpl(TaskRepository taskRepository, TaskMapper taskMapper, ProjectMapper projectMapper, UserService userService, UserMapper userMapper) {
        this.taskRepository = taskRepository;
        this.taskMapper = taskMapper;
        this.projectMapper = projectMapper;
        this.userService = userService;
        this.userMapper = userMapper;
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
            convertedTask.setTaskStatus(dto.getTaskStatus()== null ? task.get().getTaskStatus() : dto.getTaskStatus());
            //if there is no status, get it from DB, if there is, then get it from user
            convertedTask.setAssignedDate(task.get().getAssignedDate());
            taskRepository.save(convertedTask);
        }
    }

    @Override
    public void deleteById(Long id) {
        Optional<Task> foundTask = taskRepository.findById(id);

        if(foundTask.isPresent()){
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
    @Override
    public Integer totalNonCompletedTask(String projectCode) {
        return taskRepository.totalNonCompletedTask(projectCode);
    }

    @Override
    public Integer totalCompletedTask(String projectCode) {
        return taskRepository.totalCompletedTask(projectCode);
    }

    @Override
    public void deleteByProject(ProjectDTO projectDTO) {
        Project project=projectMapper.convertToEntity(projectDTO);
        List<Task> tasks=taskRepository.findAllByProject(project);
        tasks.forEach(task -> deleteById(task.getId()));// use deleteById()

    }

    @Override
    public void completeByProject(ProjectDTO projectDTO) {
        Project project=projectMapper.convertToEntity(projectDTO);
        List<Task> tasks=taskRepository.findAllByProject(project);
        tasks.stream().map(taskMapper::convertToDTO).forEach(taskDTO ->
        {
            taskDTO.setTaskStatus(Status.COMPLETE);
            update(taskDTO);// back to update()
        });

    }

    @Override
    public List<TaskDTO> listAllTasksByStatusIsNot(Status status) {
        UserDTO loggedIn = userService.findByUserName("harold@manager.com");
        List<Task> tasks=taskRepository.findAllByTaskStatusIsNotAndAssignedEmployee(status, userMapper.convertToEntity(loggedIn));

        return tasks.stream().map(taskMapper::convertToDTO).collect(Collectors.toList());
    }

    @Override
    public List<TaskDTO> listAllTasksByStatus(Status status) {
        UserDTO loggedIn = userService.findByUserName("harold@manager.com");
        List<Task> tasks=taskRepository.findAllByTaskStatusAndAssignedEmployee(status, userMapper.convertToEntity(loggedIn));

        return tasks.stream().map(taskMapper::convertToDTO).collect(Collectors.toList());
    }
}
