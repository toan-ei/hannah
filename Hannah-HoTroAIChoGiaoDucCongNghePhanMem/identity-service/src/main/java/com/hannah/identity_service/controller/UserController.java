package com.hannah.identity_service.controller;

import com.hannah.identity_service.dto.request.CreateUserRequest;
import com.hannah.identity_service.dto.request.UpdateUserRequest;
import com.hannah.identity_service.dto.response.ApiResponse;
import com.hannah.identity_service.dto.response.UserResponse;
import com.hannah.identity_service.service.UserService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserController {
    UserService userService;

    @PostMapping("createUser")
    public ApiResponse<UserResponse> createUser(@RequestBody CreateUserRequest request){
        UserResponse userResponse = userService.createUser(request);
        return ApiResponse.<UserResponse>builder()
                .code(1001)
                .result(userResponse)
                .build();
    }

    @GetMapping("/getUser/{userId}")
    public ApiResponse<UserResponse> getUser(@PathVariable String userId){
        UserResponse userResponse = userService.getUser(userId);
        return ApiResponse.<UserResponse>builder()
                .code(1001)
                .result(userResponse)
                .build();
    }

    @GetMapping("/getAllUser")
    public ApiResponse<List<UserResponse>> getAllUser(){
        List<UserResponse> userResponses = userService.getAllUser();
        return ApiResponse.<List<UserResponse>>builder()
                .code(1001)
                .result(userResponses)
                .build();
    }

    @PutMapping("/updateUser/{userId}")
    public ApiResponse<UserResponse> updateUser(@RequestBody UpdateUserRequest request, @PathVariable String userId){
        UserResponse userResponse = userService.updateUser(request, userId);
        return ApiResponse.<UserResponse>builder()
                .code(1001)
                .result(userResponse)
                .build();
    }

    @DeleteMapping("/deleteUser/{userId}")
    public ApiResponse<Void> deleteUser(@PathVariable String userId){
        userService.deleteUser(userId);
        return ApiResponse.<Void>builder()
                .code(1001)
                .build();
    }

}
