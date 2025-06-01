package com.hannah.identity_service.controller.internal;

import com.hannah.identity_service.dto.request.CreateUserRequest;
import com.hannah.identity_service.dto.request.UpdateUserRequest;
import com.hannah.identity_service.dto.response.ApiResponse;
import com.hannah.identity_service.dto.response.UserResponse;
import com.hannah.identity_service.service.UserService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/internal/users")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class InternalUserController {
    UserService userService;

    @DeleteMapping("/deleteUser/{userId}")
    public ApiResponse<Void> deleteUser(@PathVariable String userId){
        userService.deleteUser(userId);
        return ApiResponse.<Void>builder()
                .code(1001)
                .build();
    }

}
