package com.mntn.controllers;

import com.mntn.pojo.Message;
import com.mntn.services.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:3000")
public class ApiChatController {

    @Autowired
    private ChatService chatService;

    @PostMapping("/chat/send")
    public ResponseEntity<String> sendMessage(@RequestBody Message message) {
        try {
            chatService.sendMessage(message);
            return ResponseEntity.ok("Message sent successfully");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error sending message");
        }
    }

    @GetMapping("/chat/rooms/{userId}")
    public ResponseEntity<String> getRoomId(@PathVariable(value = "userId") String userId) {
        try {
            String roomId = chatService.getRoomId(userId);
            return ResponseEntity.ok(roomId);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error generating room ID");
        }
    }

    @GetMapping("/chat/messages/{roomId}")
    public ResponseEntity<List<Message>> getChatMessages(@PathVariable(value = "roomId") String roomId) {
        try {
            List<Message> messages = chatService.getChatMessages(roomId);
            return ResponseEntity.ok(messages);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(new ArrayList<>());
        }
    }

    @GetMapping("/chat/rooms")
    public ResponseEntity<List<Map<String, Object>>> getAllChatRooms() {
        try {
            List<Map<String, Object>> chatRooms = chatService.getAllChatRooms();
            return ResponseEntity.ok(chatRooms);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(new ArrayList<>());
        }
    }
}