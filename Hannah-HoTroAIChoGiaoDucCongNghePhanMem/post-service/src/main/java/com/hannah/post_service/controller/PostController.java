package com.hannah.post_service.controller;

import com.hannah.post_service.dto.request.PostRequest;
import com.hannah.post_service.dto.response.ApiResponse;
import com.hannah.post_service.dto.response.PageResponse;
import com.hannah.post_service.dto.response.PostResponse;
import com.hannah.post_service.service.PostService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PostController {
    PostService postService;

    @PostMapping("/createPost")
    public ApiResponse<PostResponse> createPost(@RequestBody PostRequest request){
        PostResponse postResponse = postService.createPost(request);
        return ApiResponse.<PostResponse>builder()
                .result(postResponse)
                .build();
    }

    @PostMapping("/createVideoPost")
    public ApiResponse<PostResponse> createVideoPost(@RequestParam(name = "media") MultipartFile file){
        PostResponse postResponse = postService.createVideoPost(file);
        return ApiResponse.<PostResponse>builder()
                .result(postResponse)
                .build();
    }

    @GetMapping("/myPosts")
    public ApiResponse<PageResponse<PostResponse>> myPosts(
            @RequestParam(value = "page", required = false, defaultValue = "1") int page,
            @RequestParam(value = "size", required = false, defaultValue = "10") int size){
        PageResponse<PostResponse> postResponses = postService.getListPosts(page, size);
        return ApiResponse.<PageResponse<PostResponse>>builder()
                .result(postResponses)
                .build();
    }

}
