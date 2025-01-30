package com.eastbarnetschool.ordermatchingengine.api.config;

import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.Map;

public class AuthenticatedWebSocketHandler extends TextWebSocketHandler {

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        Map<String, Object> attributes = session.getAttributes();
        String userId = (String) attributes.get("user");

        if (userId == null) {
            System.out.println("❌ No authenticated user found!");
            session.close();  // Reject connection
            return;
        }

        System.out.println("✅ WebSocket connection authenticated for user: " + userId);
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        session.sendMessage(new TextMessage("Hello, " + session.getAttributes().get("user") + "!"));
    }
}
