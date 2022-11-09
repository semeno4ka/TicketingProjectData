package com.cydeo.service.impl;

import com.cydeo.dto.UserDTO;
import com.cydeo.entity.User;
import com.cydeo.mapper.UserMapper;
import com.cydeo.repository.UserRepository;
import com.cydeo.service.UserService;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
//@Transactional// will roll back all methods if any error occurs. Delete, Insert, Update etc actions
//@Modifying is used with JPQL and SQL queries and is same as Transactional (method level)
public class UserServiceImpl implements UserService {
    final private UserRepository userRepository;
    final private UserMapper userMapper;

    public UserServiceImpl(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    @Override
    public List<UserDTO> listAllUsers() {
       List<User> userList= userRepository.findAll(Sort.by("firstName"));
       return userList.stream().map(userMapper::convertToDTO).collect(Collectors.toList());
    }

    @Override
    public UserDTO findByUserName(String username) {
        return userMapper.convertToDTO(userRepository.findByUserName(username));
    }

    @Override
    public void save(UserDTO user) {
    userRepository.save(userMapper.convertToEntity(user));
    }

    @Override
    public void deleteByUserName(String username) {
      userRepository.deleteByUserName(username);
    }

    @Override
    public void delete(String username) {
        User user=userRepository.findByUserName(username);
        user.setIsDeleted(true);
        userRepository.save(user);
    }

    @Override
    public UserDTO update(UserDTO userDto) {
        //find current user
        User user1=userRepository.findByUserName(userDto.getUserName());
        //Convert to Entity
        User convertedUser=userMapper.convertToEntity(userDto);//doesn't have id
        //to avoid creating another user with new ID, we set the ID from previous
        convertedUser.setId(user1.getId());
        //save updated
        userRepository.save(convertedUser);
        return findByUserName(userDto.getUserName());//why do we return username?

    }
}
