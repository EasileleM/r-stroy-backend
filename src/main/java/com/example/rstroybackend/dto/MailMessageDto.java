package com.example.rstroybackend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
public class MailMessageDto {
    @NotBlank
    private String subject;
    @NotBlank
    private String message;
}
