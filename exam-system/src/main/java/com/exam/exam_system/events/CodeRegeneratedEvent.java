package com.exam.exam_system.events;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CodeRegeneratedEvent {
    private String email;
    private String username;
    private String code;
}