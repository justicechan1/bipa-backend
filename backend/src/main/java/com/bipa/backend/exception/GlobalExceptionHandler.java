package com.bipa.backend.exception;

import com.bipa.backend.dto.common.ErrorCode;
import com.bipa.backend.dto.common.ErrorResponse;
import org.springframework.http.*;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFound(NotFoundException e) {
        return ResponseEntity.badRequest()
                .body(new ErrorResponse("error", ErrorCode.PLACE_NOT_FOUND, e.getMessage()));
    }

    @ExceptionHandler(InvalidInputException.class)
    public ResponseEntity<ErrorResponse> handleInvalid(InvalidInputException e) {
        return ResponseEntity.badRequest()
                .body(new ErrorResponse("error", ErrorCode.INVALID_INPUT, e.getMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidation(MethodArgumentNotValidException e) {
        String msg = e.getBindingResult().getFieldErrors().stream()
                .findFirst().map(fe -> fe.getField() + " " + fe.getDefaultMessage())
                .orElse("Invalid input");
        return ResponseEntity.badRequest()
                .body(new ErrorResponse("error", ErrorCode.INVALID_INPUT, msg));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleEtc(Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorResponse("error", ErrorCode.INTERNAL_ERROR, "서버 내부 오류가 발생했습니다."));
    }
}
