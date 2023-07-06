package com.naqi.invitation.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.naqi.invitation.models.Message;
import com.naqi.invitation.repositories.MessageRepository;

@Service
public class MessageService {

    @Autowired
    private final MessageRepository messageRepository;

    public MessageService(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    public List<Message> getAllMessages() {
        return messageRepository.findAll();
    }

    public Message createMessage(Message message) {
        return messageRepository.save(message);
    }

    public Page<Message> getMessagesAfterId(int lastMessageId, int pageSize) {
        Sort sort = Sort.by(Sort.Direction.DESC, "id");
        PageRequest pageRequest = PageRequest.of(0, pageSize, sort);

        if (lastMessageId != 0) {
            return messageRepository.findByIdGreaterThan(lastMessageId, pageRequest);
        } else {
            return messageRepository.findAll(pageRequest);
        }
    }

}
