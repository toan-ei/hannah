package com.hannah.post_service.mapper;

import com.hannah.post_service.Entity.Post;
import com.hannah.post_service.dto.response.PostResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PostMapper {
    PostResponse toPostResponse(Post post);
}
