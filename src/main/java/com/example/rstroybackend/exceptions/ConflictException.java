package com.example.rstroybackend.exceptions;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.Map;

@ResponseStatus(value = HttpStatus.CONFLICT)
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ConflictException extends RuntimeException {
    private Map<Object, Object> body;
}
