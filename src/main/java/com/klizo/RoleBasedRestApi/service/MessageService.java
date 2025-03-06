package com.klizo.RoleBasedRestApi.service;

import com.klizo.RoleBasedRestApi.model.Message;
import com.klizo.RoleBasedRestApi.repository.MessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MessageService {

    @Autowired
    private final MessageRepository messageRepository;
    @Autowired
    private final ChatRoomService chatRoomService;



    public Message save(Message message) {
        var chatId = chatRoomService
                .getChatRoomId(message.getSenderId(), message.getReceiverId(), true)
                .orElseThrow(() -> new RuntimeException("Chat room could not be created or found"));
        message.setChatId(chatId);
        messageRepository.save(message);
        return message;
    }

    public List<Message> getChatMessage(String senderId, String receiverId) {
        var chatId = chatRoomService.getChatRoomId(senderId, receiverId, false);
        return chatId.map(messageRepository::findByChatId).orElse(new ArrayList<>());
    }
}