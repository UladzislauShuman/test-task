package com.example.java_ifortex_test_task.repository;

import com.example.java_ifortex_test_task.entity.DeviceType;
import com.example.java_ifortex_test_task.entity.Session;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface SessionRepository extends JpaRepository<Session, Long> {

    interface SessionRaw {
        Long getId();
        Integer getDeviceType();
        LocalDateTime getEndedAtUtc();
        LocalDateTime getStartedAtUtc();
        Long getUserId();
    }

    @Query(value = """
        SELECT
                s.id as id,
                s.device_type as deviceType,
                s.ended_at_utc as endedAtUtc,
                s.started_at_utc as startedAtUtc,
                s.user_id as userId
        FROM sessions s
        WHERE device_type = :#{#deviceType.getCode()}
        ORDER BY started_at_utc ASC
        LIMIT 1
        """, nativeQuery = true)
    SessionRaw getFirstDesktopSession(@Param("deviceType") DeviceType deviceType);


    @Query(value = """
    SELECT 
        s.id as id,
        s.device_type as deviceType,
        s.ended_at_utc as endedAtUtc,
        s.started_at_utc as startedAtUtc,
        s.user_id as userId
    FROM sessions s
    JOIN users u ON s.user_id = u.id
    WHERE u.deleted = false
        AND s.ended_at_utc < :endDate
    ORDER BY s.started_at_utc DESC
    """, nativeQuery = true)
    List<SessionRaw> getSessionsFromActiveUsersEndedBefore2025(@Param("endDate") LocalDateTime endDate);
}