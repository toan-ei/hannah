package com.hannah.AI_service.mapper;

import com.hannah.AI_service.dto.response.ChatResponse;
import com.hannah.AI_service.entity.Chat;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ChatMapper {
    ChatResponse toChatResponse(Chat chat);
}
