package com.hannah.post_service.service;

import com.hannah.post_service.Entity.Post;
import com.hannah.post_service.dto.request.PostRequest;
import com.hannah.post_service.dto.response.PageResponse;
import com.hannah.post_service.dto.response.PostResponse;
import com.hannah.post_service.dto.response.ProfileResponse;
import com.hannah.post_service.mapper.PostMapper;
import com.hannah.post_service.repository.PostRepository;
import com.hannah.post_service.repository.httpclient.ProfileClient;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
@Slf4j
public class PostService {
    PostRepository postRepository;
    PostMapper postMapper;
    DateTimeFormatter dateTimeFormatter;
    ProfileClient profileClient;

    public PostResponse createPost(PostRequest postRequest){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Post post = Post.builder()
                .content(postRequest.getContent())
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

        var data = postRepository.findAllByUserId(userId, pageable);

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

}
