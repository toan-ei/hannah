package com.hannah.post_service.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hannah.post_service.dto.request.PostRequest;
import com.hannah.post_service.dto.request.PostVideoRequest;
import com.hannah.post_service.dto.response.ApiResponse;
import com.hannah.post_service.dto.response.PageResponse;
import com.hannah.post_service.dto.response.PostResponse;
import com.hannah.post_service.service.PostService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class PostController {
    PostService postService;

    @PostMapping("/createPost")
    public ApiResponse<PostResponse> createPost(@RequestBody PostRequest request){
        PostResponse postResponse = postService.createPost(request);
        return ApiResponse.<PostResponse>builder()
                .result(postResponse)
                .build();
    }

    @PostMapping(value = "/createVideoPost", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiResponse<PostResponse> createVideoPost(
            @RequestPart("media") MultipartFile file,
            @RequestParam("request") String requestJson
    ) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        PostVideoRequest request = mapper.readValue(requestJson, PostVideoRequest.class);

        return ApiResponse.<PostResponse>builder()
                .result(postService.createVideoPost(file, request))
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
    @GetMapping("/teacherPosts/{userId}")
    public ApiResponse<List<PostResponse>> teacherPosts(@PathVariable String userId){
        List<PostResponse> postResponses = postService.getTeacherListPosts(userId);
        return ApiResponse.<List<PostResponse>>builder()
                .result(postResponses)
                .build();
    }

}
