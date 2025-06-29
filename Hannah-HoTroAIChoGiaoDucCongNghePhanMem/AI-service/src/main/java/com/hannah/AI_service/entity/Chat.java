package com.hannah.AI_service.entity;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.time.Instant;
import java.util.Date;

@Document(value = "Chat")
@Getter
@Setter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Chat {
    @MongoId
    String id;
    String userId;
    String message;
    String responseOfAssistant;
    Instant createdTime;
}
