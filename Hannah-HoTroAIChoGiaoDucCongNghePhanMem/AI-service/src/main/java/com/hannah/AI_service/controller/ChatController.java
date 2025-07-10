package com.hannah.AI_service.controller;

import com.hannah.AI_service.dto.request.ChatRequest;
import com.hannah.AI_service.dto.response.ApiResponse;
import com.hannah.AI_service.dto.response.ChatResponse;
import com.hannah.AI_service.dto.response.PageResponse;
import com.hannah.AI_service.service.ChatService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/history")
    public ApiResponse<PageResponse<ChatResponse>> history(
            @RequestParam(value = "page", required = false, defaultValue = "1") int page,
            @RequestParam(value = "size", required = false, defaultValue = "10") int size
    ){
        PageResponse<ChatResponse> history = chatService.history(page, size);
        return ApiResponse.<PageResponse<ChatResponse>>builder()
                .result(history)
                .build();
    }
}
