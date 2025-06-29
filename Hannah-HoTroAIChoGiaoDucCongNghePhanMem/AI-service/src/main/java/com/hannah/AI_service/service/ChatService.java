package com.hannah.AI_service.service;

import com.hannah.AI_service.dto.request.ChatRequest;
import com.hannah.AI_service.dto.response.ChatResponse;
import com.hannah.AI_service.entity.Chat;
import com.hannah.AI_service.repository.ChatRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;


@Service
@Slf4j
public class ChatService {
    private final ChatClient chatClient;
    private final ChatRepository chatRepository;
    public ChatService(ChatClient.Builder builder, ChatRepository chatRepository){
        this.chatClient = builder.build();
        this.chatRepository = chatRepository;
    }

    public ChatResponse chat(ChatRequest request){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userId = authentication.getName();
        log.info("userId {}", userId);

        Sort sort = Sort.by("createdTime").descending();
        Pageable pageable = PageRequest.of(0, 10, sort);
        Page<Chat> allByUserId = chatRepository.findAllByUserId(userId, pageable);
        List<Chat> previousQuestionList = allByUserId.getContent();

        ChatMemory chatMemory = MessageWindowChatMemory.builder()
                .maxMessages(10)
                .build();

        for (int i = previousQuestionList.size() - 1; i >= 0 ; i--){
            Chat chat = previousQuestionList.get(i);
            chatMemory.add(userId, new UserMessage(chat.getMessage()));
            chatMemory.add(userId, new AssistantMessage(chat.getResponseOfAssistant()));
        }

        UserMessage newMessage = new UserMessage(request.getMessage());
        chatMemory.add(userId, newMessage);

        Prompt prompt = new Prompt(chatMemory.get(userId));
        String content = chatClient.prompt(prompt).call().content();
        chatMemory.add(userId, new AssistantMessage(content));

        Chat chat = Chat.builder()
                .userId(userId)
                .message(request.getMessage())
                .responseOfAssistant(content)
                .createdTime(Instant.now())
                .build();

        chatRepository.save(chat);
        return ChatResponse.builder()
                .userId(userId)
                .message(content)
                .build();
    }
}
