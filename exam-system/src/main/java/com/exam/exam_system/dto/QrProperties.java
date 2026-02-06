package com.exam.exam_system.dto;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;

@Component
@ConfigurationProperties(prefix = "exam.qr")
@Getter
@Setter
public class QrProperties {

	private long expirationMinutes;
}
