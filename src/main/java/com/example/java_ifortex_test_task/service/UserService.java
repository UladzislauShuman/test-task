package com.example.java_ifortex_test_task.service;

import com.example.java_ifortex_test_task.dto.UserResponseDTO;
import com.example.java_ifortex_test_task.entity.DeviceType;
import com.example.java_ifortex_test_task.entity.User;
import com.example.java_ifortex_test_task.mapper.UserMapper;
import com.example.java_ifortex_test_task.repository.SessionRepository;
import com.example.java_ifortex_test_task.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {
    public static final String USER_WITH_MOST_OF_SESSIONS_NOT_FOUND_MESSAGE = "User with most of sessions not found";
    private final UserRepository userRepository;
    private final SessionRepository sessionRepository;
    private final UserMapper userMapper;

    // Returns a User with the biggest amount of sessions
    public UserResponseDTO getUserWithMostSessions() {
        User user = userRepository.getUserWithMostSessions();
        if (user == null) {
            throw new EntityNotFoundException(USER_WITH_MOST_OF_SESSIONS_NOT_FOUND_MESSAGE);
        }
        return userMapper.toDto(user);
    }

    // Returns Users that have at least 1 Mobile session
    public List<UserResponseDTO> getUsersWithAtLeastOneMobileSession() {
        List<User> users = userRepository.getUsersWithAtLeastOneMobileSession(DeviceType.MOBILE);
        return users.stream()
                .map(userMapper::toDto)
                .collect(Collectors.toList());
    }
}
