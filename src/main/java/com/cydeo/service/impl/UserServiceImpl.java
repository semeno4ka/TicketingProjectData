package com.cydeo.service.impl;

import com.cydeo.dto.ProjectDTO;
import com.cydeo.dto.TaskDTO;
import com.cydeo.dto.UserDTO;
import com.cydeo.entity.User;
import com.cydeo.mapper.UserMapper;
import com.cydeo.repository.UserRepository;
import com.cydeo.service.ProjectService;
import com.cydeo.service.TaskService;
import com.cydeo.service.UserService;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
//@Transactional// will roll back all methods if any error occurs. Delete, Insert, Update etc actions
//@Modifying is used with JPQL and SQL queries and is same as Transactional (method level)
public class UserServiceImpl implements UserService {
    final private UserRepository userRepository;
    final private UserMapper userMapper;
    final private ProjectService projectService;
    private final TaskService taskService;

    public UserServiceImpl(UserRepository userRepository, UserMapper userMapper, @Lazy ProjectService projectService,@Lazy TaskService taskService) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.projectService = projectService;
        this.taskService = taskService;
    }

    @Override
    public List<UserDTO> listAllUsers() {
       List<User> userList= userRepository.findAllByIsDeletedOrderByFirstNameDesc(false);
       return userList.stream().map(userMapper::convertToDTO).collect(Collectors.toList());
    }

    @Override
    public UserDTO findByUserName(String username) {
        User user = userRepository.findByUserNameAndIsDeleted(username, false);
        return userMapper.convertToDTO(user);
    }

    @Override
    public void save(UserDTO user) {
    userRepository.save(userMapper.convertToEntity(user));
    }

//    @Override
//    public void deleteByUserName(String username) {
//      userRepository.deleteByUserName(username);
//    }hard delete

    @Override
    public void delete(String username) {//soft delete
        User user = userRepository.findByUserNameAndIsDeleted(username,false);
        if (checkIfUserCanBeDeleted(user)) {
            user.setIsDeleted(true);
            user.setUserName(user.getUserName()+"-"+user.getId());// make it unique after deletion and yet allow to use previous username
            userRepository.save(user);
        }
        //can through exception that cannot be deleted
    }

    @Override
    public UserDTO update(UserDTO user) {
        //find current user
        User user1 = userRepository.findByUserNameAndIsDeleted(user.getUserName(), false);
        //Convert to Entity
        User convertedUser = userMapper.convertToEntity(user);//doesn't have id
        //to avoid creating another user with new ID, we set the ID from previous
        convertedUser.setId(user1.getId());
        //save updated
        userRepository.save(convertedUser);
        return findByUserName(user.getUserName());//why do we return username?

    }

    @Override
    public List<UserDTO> listAllByRole(String role) {
        List<User> users= userRepository.findByRoleDescriptionIgnoreCaseAndIsDeleted(role, false);
        return users.stream().map(userMapper::convertToDTO).collect(Collectors.toList());
    }

    private boolean checkIfUserCanBeDeleted(User user) {

        switch (user.getRole().getDescription()) {
            case "Manager":
                List<ProjectDTO> projectDTOList = projectService.listAllNonCompletedByAssignedManager(userMapper.convertToDTO(user));
                return projectDTOList.size() == 0;
            case "Employee":
                List<TaskDTO> taskDTOList = taskService.listAllNonCompletedByAssignedEmployee(userMapper.convertToDTO(user));
                return taskDTOList.size() == 0;
            default:
                return true;
        }

    }
}
