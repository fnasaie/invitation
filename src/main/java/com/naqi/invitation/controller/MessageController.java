package com.naqi.invitation.controller;

import com.naqi.invitation.models.Message;
import com.naqi.invitation.repositories.MessageRepository;
import com.naqi.invitation.services.MessageService;
import com.naqi.invitation.utility.HttpResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Sort;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/messages")
public class MessageController {

    @Autowired
    MessageRepository messageRepository;

    @Autowired
    MessageService messageService;
    

    // @MessageMapping("/sendMessage")
    // @SendTo("/topic/messages")
    // public ResponseEntity<HttpResponse> createMessage(HttpServletRequest request, @RequestBody Message message) {
    //     HttpResponse response = new HttpResponse(request.getRequestURI());

    //     try {
    //         Message newMessage = messageService.createMessage(message);

    //         response.setStatus(HttpStatus.OK);
    //         response.setData(newMessage);
    //     } catch (Exception e) {
    //         response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
    //         response.setMessage("Unexpected error creating message: " + e.getMessage());
    //     }
    //     return ResponseEntity.status(response.getStatus()).body(response);
    // }

    @PostMapping("/sendMessage")
    public ResponseEntity<HttpResponse> createMessage(HttpServletRequest request, @RequestBody Message message) {
        HttpResponse response = new HttpResponse(request.getRequestURI());

        try {
            messageService.createMessage(message);

            List<Message> messageList = messageRepository.findAll(Sort.by(Sort.Direction.DESC, "created"));
            response.setStatus(HttpStatus.OK);
            response.setData(messageList);
        } catch (Exception e) {
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
            response.setMessage("Unexpected error creating message: " + e.getMessage());
        }
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @GetMapping("/getMessage")
    public ResponseEntity<HttpResponse> getAllMessages(HttpServletRequest request) {
        HttpResponse response = new HttpResponse(request.getRequestURI());

        try {
            List<Message> messageList = messageRepository.findAll(Sort.by(Sort.Direction.DESC, "created"));
            response.setStatus(HttpStatus.OK);
            response.setData(messageList);
        } catch (Exception e) {
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
            response.setMessage("Unexpected error getting messages: " + e.getMessage());
        }
        return ResponseEntity.status(response.getStatus()).body(response);
    }
}
