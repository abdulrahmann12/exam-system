package com.exam.exam_system.events;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmailChangeEvent {

    private String newEmail;
    private String username;
    private String code;
}
