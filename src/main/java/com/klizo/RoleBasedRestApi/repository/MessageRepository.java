package com.klizo.RoleBasedRestApi.repository;

import com.klizo.RoleBasedRestApi.model.Message;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, String> {
    List<Message> findByChatId(String chatId);
}