package com.hannah.AI_service.controller;

import com.hannah.AI_service.dto.request.ChatRequest;
import com.hannah.AI_service.dto.response.ApiResponse;
import com.hannah.AI_service.dto.response.ChatResponse;
import com.hannah.AI_service.service.ChatService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ChatController {
    ChatService chatService;

    @PostMapping("/ask")
    public ApiResponse<ChatResponse> chat(@RequestBody ChatRequest request){
        return ApiResponse.<ChatResponse>builder()
                .result(chatService.chat(request))
                .build();
    }
}
