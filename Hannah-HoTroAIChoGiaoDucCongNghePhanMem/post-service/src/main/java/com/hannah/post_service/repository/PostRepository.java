package com.hannah.post_service.repository;

import com.hannah.post_service.Entity.Post;
import com.hannah.post_service.dto.response.PageResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;


public interface PostRepository extends MongoRepository<Post, String> {
    Page<Post> findAllByUserId(String userId , Pageable pageable);
}
