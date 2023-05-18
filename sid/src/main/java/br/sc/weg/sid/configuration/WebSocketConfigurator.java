package br.sc.weg.sid.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketTransportRegistration;

@Configuration
@CrossOrigin
@EnableWebSocketMessageBroker
public class WebSocketConfigurator implements WebSocketMessageBrokerConfigurer {
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws").setAllowedOriginPatterns("*").withSockJS()
                //2gb
                .setStreamBytesLimit(200000)
                //1gb
                .setHttpMessageCacheSize(10000)
                //30 segundos
                .setDisconnectDelay(30 * 10000);
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.setApplicationDestinationPrefixes("/app");
        registry.setUserDestinationPrefix("/demanda");
    }

    @Override
    public void configureWebSocketTransport(WebSocketTransportRegistration registration) {
        //2gb
        registration.setMessageSizeLimit(2 * 1024 * 1024);
    }
}


















