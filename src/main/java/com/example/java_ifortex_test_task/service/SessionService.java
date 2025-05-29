package com.example.java_ifortex_test_task.service;

import com.example.java_ifortex_test_task.dto.SessionResponseDTO;
import com.example.java_ifortex_test_task.entity.DeviceType;
import com.example.java_ifortex_test_task.entity.Session;
import com.example.java_ifortex_test_task.entity.User;
import com.example.java_ifortex_test_task.mapper.SessionMapper;
import com.example.java_ifortex_test_task.repository.SessionRepository;
import com.example.java_ifortex_test_task.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SessionService {
    private static final String SESSION_NOT_FOUND = "session not found";
    private static final LocalDateTime endDate = LocalDateTime.of(2025, 1, 1, 0, 0);

    private final SessionRepository sessionRepository;
    private final UserRepository userRepository;
    private final SessionMapper sessionMapper;

    // Returns the first (earliest) desktop Session
    public SessionResponseDTO getFirstDesktopSession() {
        SessionRepository.SessionRaw sessionRaw = sessionRepository.getFirstDesktopSession(DeviceType.DESKTOP);
        Session session = convertToSession(sessionRaw);
        if (session == null) {
            throw new EntityNotFoundException(SESSION_NOT_FOUND);
        }
        return sessionMapper.toDto(session);
    }

    // Returns only Sessions from Active users that were ended before 2025
    public List<SessionResponseDTO> getSessionsFromActiveUsersEndedBefore2025() {
        List<Session> sessions = sessionRepository.getSessionsFromActiveUsersEndedBefore2025(endDate).stream().map(this::convertToSession).toList();
        return sessions.stream()
                .map(sessionMapper::toDto)
                .collect(Collectors.toList());
    }

    private Session convertToSession(SessionRepository.SessionRaw sessionRaw) {
        User user = userRepository.findById(sessionRaw.getUserId()).orElseThrow();
        return Session.builder()
                .id(sessionRaw.getId())
                .deviceType(DeviceType.fromCode(sessionRaw.getDeviceType()))
                .endedAtUtc(sessionRaw.getEndedAtUtc())
                .startedAtUtc(sessionRaw.getStartedAtUtc())
                .user(user)
                .build();
    }
}