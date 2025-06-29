package com.hannah.AI_service.repository;

import com.hannah.AI_service.entity.Chat;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ChatRepository extends MongoRepository<Chat, String> {
    Page<Chat> findAllByUserId(String userId, Pageable pageable);
}
