package com.mntn.services;

import com.google.cloud.firestore.*;
import com.mntn.pojo.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

@Service
public class ChatService {

    @Autowired
    private Firestore firestore;

    public void sendMessage(Message message) {
        try {
            DocumentReference docRef = firestore.collection("messages").document();
            message.setId(docRef.getId());

            if (message.getTimestamp() == 0) {
                message.setTimestamp(System.currentTimeMillis());
            }

            docRef.set(message).get();

        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException("Failed to send message", e);
        }
    }

    public String getRoomId(String userId) {
        if (userId == null || userId.trim().isEmpty()) {
            throw new IllegalArgumentException("User ID cannot be null or empty");
        }
        return "user_admin_" + userId.trim().toLowerCase();
    }

    public List<Message> getChatMessages(String chatRoomId) {
        try {
            Query query = firestore.collection("messages")
                    .whereEqualTo("chatRoomId", chatRoomId)
                    .orderBy("timestamp", Query.Direction.ASCENDING);

            QuerySnapshot querySnapshot = query.get().get();
            List<Message> messageList = new ArrayList<>();

            for (DocumentSnapshot document : querySnapshot.getDocuments()) {
                Message message = document.toObject(Message.class);
                if (message != null) {
                    messageList.add(message);
                }
            }

            return messageList;
        } catch (InterruptedException | ExecutionException e) {
            return new ArrayList<>();
        }
    }

    public List<Map<String, Object>> getAllChatRooms() {
        try {
            // Query tất cả messages và filter trong code
            Query query = firestore.collection("messages")
                    .orderBy("timestamp", Query.Direction.DESCENDING);

            QuerySnapshot querySnapshot = query.get().get();
            Map<String, Map<String, Object>> chatRoomsMap = new HashMap<>();

            for (DocumentSnapshot document : querySnapshot.getDocuments()) {
                Message message = document.toObject(Message.class);
                if (message != null && message.getChatRoomId() != null) {
                    String chatRoomId = message.getChatRoomId();

                    // Filter messages có chatRoomId chứa "user_admin_"
                    if (chatRoomId.contains("user_admin_")) {
                        // Chỉ lấy message mới nhất của mỗi chat room
                        if (!chatRoomsMap.containsKey(chatRoomId)) {
                            String userId = chatRoomId.substring("user_admin_".length());

                            Map<String, Object> chatInfo = new HashMap<>();
                            chatInfo.put("userId", userId);
                            chatInfo.put("chatRoomId", chatRoomId);
                            chatInfo.put("lastMessage", message.getContent());
                            chatInfo.put("lastSender", message.getSender());
                            chatInfo.put("lastTimestamp", message.getTimestamp());

                            chatRoomsMap.put(chatRoomId, chatInfo);
                        }
                    }
                }
            }

            List<Map<String, Object>> chatList = new ArrayList<>(chatRoomsMap.values());

            // Sắp xếp theo timestamp giảm dần
            chatList.sort((a, b) -> Long.compare((Long) b.get("lastTimestamp"), (Long) a.get("lastTimestamp")));

            return chatList;

        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
}