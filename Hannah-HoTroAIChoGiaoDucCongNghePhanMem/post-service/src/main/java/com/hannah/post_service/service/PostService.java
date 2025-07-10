package com.hannah.post_service.service;

import com.hannah.post_service.Entity.Post;
import com.hannah.post_service.constant.PostType;
import com.hannah.post_service.dto.request.PostRequest;
import com.hannah.post_service.dto.request.PostVideoRequest;
import com.hannah.post_service.dto.request.ProfileListUserIDRequest;
import com.hannah.post_service.dto.response.*;
import com.hannah.post_service.mapper.PostMapper;
import com.hannah.post_service.repository.PostRepository;
import com.hannah.post_service.repository.httpclient.FileClient;
import com.hannah.post_service.repository.httpclient.ProfileClient;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
@Slf4j
public class PostService {
    PostRepository postRepository;
    PostMapper postMapper;
    DateTimeFormatter dateTimeFormatter;
    ProfileClient profileClient;
    FileClient fileClient;

    public PostResponse createPost(PostRequest postRequest){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Post post = Post.builder()
                .type(PostType.TEXT)
                .content(postRequest.getContent())
                .userId(authentication.getName())
                .createdDate(Instant.now())
                .modifiedDate(Instant.now())
                .build();
        post = postRepository.save(post);
        return postMapper.toPostResponse(post);
    }
    @PreAuthorize("hasAuthority('TEACHER')")
    public PostResponse createVideoPost(MultipartFile file, PostVideoRequest request){
        log.info("2");
        ApiResponse<FileResponse> fileResponseApiResponse = fileClient.uploadMedia(file);
        log.info("3");
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Post post = Post.builder()
                .title(request.getTitle())
                .type(PostType.VIDEO)
                .video(fileResponseApiResponse.getResult().getUrl())
                .userId(authentication.getName())
                .createdDate(Instant.now())
                .modifiedDate(Instant.now())
                .build();
        post = postRepository.save(post);
        return postMapper.toPostResponse(post);
    }

    public PageResponse<PostResponse> getListPosts(int page, int size){
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();

        Sort sort = Sort.by("createdDate").descending();
        Pageable pageable = PageRequest.of(page - 1, size, sort);

        var data = postRepository.findAllByUserIdAndType(userId, PostType.TEXT, pageable);

        ProfileResponse profile = null;
        try{
            profile = profileClient.getProfileFromUserId(userId).getResult();
        }
        catch (Exception e){
            log.error("Error while getting name profile", e);
        }
        String fullName = profile!= null ? profile.getFullName() : null;
        var postList = data.getContent().stream().map(post -> {
            var postResponse = postMapper.toPostResponse(post);
            postResponse.setCreatedTime(dateTimeFormatter.format(post.getCreatedDate()));
            postResponse.setFullName(fullName);
            return postResponse;
        }).toList();

        return PageResponse.<PostResponse>builder()
                .currentPage(page)
                .totalPage(data.getTotalPages())
                .pageSize(data.getSize())
                .totalElements(data.getTotalElements())
                .data(postList)
                .build();
    }

    public List<PostResponse> getTeacherListPosts(String userId){
        List<Post> allByUserId = postRepository.findAllByUserId(userId);
        return allByUserId.stream().map(postMapper::toPostResponse).collect(Collectors.toList());
    }

    public PageResponse<PostResponse> getAllPosts(int page, int size){
        Sort sort = Sort.by("createdDate").descending();
        Pageable pageable = PageRequest.of(page - 1, size, sort);
        Page<Post> allPosts = postRepository.findByType(PostType.TEXT ,pageable);
        List<String> userIds = new ArrayList<>();
        List<PostResponse> postList = allPosts.getContent().stream().map(post -> {
            PostResponse postResponse = postMapper.toPostResponse(post);
            postResponse.setCreatedTime(dateTimeFormatter.format(post.getCreatedDate()));

            String userId = post.getUserId();
            userIds.add(userId);
            postResponse.setFullName(post.getUserId());
            return postResponse;
        }).toList();

        ProfileListUserIDRequest request = ProfileListUserIDRequest.builder()
                .userIds(userIds)
                .build();

        ApiResponse<List<ProfileResponse>> profilesFromUserIds = profileClient.getProfilesFromUserIds(request);
        List<ProfileResponse> profileResponseList = profilesFromUserIds.getResult();
        Map<String, String> userIdToFullName = new LinkedHashMap<>();
        for (ProfileResponse profile : profileResponseList) {
            userIdToFullName.put(profile.getUserId(), profile.getFullName());
        }
        for (PostResponse post : postList) {
            String fullName = userIdToFullName.get(post.getUserId());
            post.setFullName(fullName);
        }

        return PageResponse.<PostResponse>builder()
                .currentPage(page)
                .pageSize(size)
                .totalPage(allPosts.getTotalPages())
                .data(postList)
                .build();
    }

}
