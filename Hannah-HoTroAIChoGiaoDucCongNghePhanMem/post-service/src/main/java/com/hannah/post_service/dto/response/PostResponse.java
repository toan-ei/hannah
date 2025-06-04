package com.hannah.post_service.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PostResponse {
    String id;
    String userId;
    String type;
    String content;
    String video;
    String fullName;
    String createdTime;
    Instant createdDate;
    Instant modifiedDate;
}
