package com.eastbarnetschool.ordermatchingengine.api.config;

import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.stereotype.Component;

@Component
public class JwtChannelInterceptor implements ChannelInterceptor {

    private final JwtDecoder jwtDecoder;

    public JwtChannelInterceptor(JwtDecoder jwtDecoder) {
        this.jwtDecoder = jwtDecoder;
    }

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);

        if (StompCommand.CONNECT.equals(accessor.getCommand())) {
            String authHeader = accessor.getFirstNativeHeader("Authorization");

            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                System.out.println("❌ No valid Authorization header found in STOMP CONNECT!");
                return null; // Reject connection
            }

            String token = authHeader.substring(7); // Extract token
            System.out.println("✅ Extracted STOMP Token: " + token);

            try {
                // 🔥 Validate the JWT token
                Jwt jwt = jwtDecoder.decode(token);
                System.out.println("✅ JWT is valid: " + jwt.getSubject());

                // Store the authenticated user in session attributes
                accessor.getSessionAttributes().put("user", jwt.getSubject());
            } catch (JwtException e) {
                System.out.println("❌ Invalid JWT: " + e.getMessage());
                return null; // Reject connection
            }
        }

        return message;
    }
}
