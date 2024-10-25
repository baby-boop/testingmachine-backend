package testingmachine_backend.socket;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.beans.factory.annotation.Autowired;

@RestController
public class WebSocketController {

    @Autowired
    private SimpleWebSocketHandler webSocketHandler;

    @PostMapping("/send-message")
    public void sendMessage(@RequestBody String message) {
        webSocketHandler.broadcastMessage(message);
    }
}
