package com.assignment.UserManagementSystem.service.user.impl;

import com.assignment.UserManagementSystem.dto.UserDto;
import com.assignment.UserManagementSystem.dto.UserSearchCriteria;
import com.assignment.UserManagementSystem.exception.ResourceNotFoundException;
import com.assignment.UserManagementSystem.model.User;
import com.assignment.UserManagementSystem.repository.UserCriteriaRepository;
import com.assignment.UserManagementSystem.repository.UserRepository;
import com.assignment.UserManagementSystem.service.user.UserService;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserCriteriaRepository userCriteriaRepository;
    private final ModelMapper modelMapper;

    @Override
    public List<UserDto> getAllUsers(UserSearchCriteria criteria) {
        return userCriteriaRepository.searchUsers(criteria)
                .stream().map(user -> modelMapper.map(user, UserDto.class)).toList();
    }

    @Override
    public UserDto getAUser(UUID id) {
        User user = userRepository.findById(id).orElseThrow(() -> {
            throw new ResourceNotFoundException("User not found with id " + id);
        });
        return modelMapper.map(user, UserDto.class);
    }
}
