package com.chat.ex.domain.chat.interceptor;

import com.chat.ex.domain.chat.service.ChatUserService;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class StompAuthInterceptor implements ChannelInterceptor {

    private final ChatUserService chatUserService;
    public StompAuthInterceptor(ChatUserService chatUserService) {
        this.chatUserService = chatUserService;
    }

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        var acc = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
        if (acc == null) {
            return message;
        }

        if (StompCommand.CONNECT.equals(acc.getCommand())) {

            String uesrIdStr = first(acc, "user-id");
            if(uesrIdStr == null){
                uesrIdStr = first(acc, "guest-id");
            }

            Long userId = (uesrIdStr != null ? Long.valueOf(uesrIdStr.replace("user-", "")) : null);

            String nickname = "익명";
            String principalName = "guest-ephemeral";

            if (userId != null) {
                var user = chatUserService.require(userId);
                nickname = user.getNickname();
                principalName = String.valueOf(user.getId());
            }

            var auth = new UsernamePasswordAuthenticationToken(principalName, null, List.of());
            acc.setUser(auth);
            acc.getSessionAttributes().put("guestId", principalName);
            acc.getSessionAttributes().put("nickname", nickname);
        }
        return message;
    }

    public String first(StompHeaderAccessor acc, String key) {
        var v = acc.getNativeHeader(key);
        return (v == null || v.isEmpty() ? null : v.get(0));
    }

}
