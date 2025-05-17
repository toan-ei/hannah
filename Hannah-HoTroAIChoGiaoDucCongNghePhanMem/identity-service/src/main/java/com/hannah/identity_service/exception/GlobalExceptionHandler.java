package com.hannah.identity_service.exception;

import com.hannah.identity_service.dto.response.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<ApiResponse> handlingRuntimeException(RuntimeException runtimeException){
        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setCode(ErrorCode.UNCATEFORIZED_EXCEPTION.getCode());
        apiResponse.setResult(ErrorCode.UNCATEFORIZED_EXCEPTION.getMessage());
        return ResponseEntity.status(ErrorCode.UNCATEFORIZED_EXCEPTION.getStatusCode()).body(apiResponse);
    }

    @ExceptionHandler(value = ApplicationException.class)
    public ResponseEntity<ApiResponse> hanglingException(ApplicationException applicationException){
        ErrorCode errorCode = applicationException.getErrorCode();
        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setCode(errorCode.getCode());
        apiResponse.setMessage(errorCode.getMessage());
        return ResponseEntity.status(errorCode.getStatusCode()).body(apiResponse);
    }

}
