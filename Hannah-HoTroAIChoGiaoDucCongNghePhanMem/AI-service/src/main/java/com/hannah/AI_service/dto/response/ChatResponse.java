package com.hannah.AI_service.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.Instant;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ChatResponse {
    String userId;
    String message;
    String responseOfAssistant;
    Instant createdTime;
    String createdTimePrint;
}
