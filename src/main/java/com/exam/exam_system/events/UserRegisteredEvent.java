package com.exam.exam_system.events;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserRegisteredEvent {

    private Long userId;
    private String email;
    private String name;
    private LocalDateTime registeredAt;
}