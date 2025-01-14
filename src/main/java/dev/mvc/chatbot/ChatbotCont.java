package dev.mvc.chatbot;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletResponse;

@RestController
public class ChatbotCont {

    @GetMapping("/chatbot")
    public String chatbot(HttpServletResponse response) {
        // HttpServletResponse 사용
        response.setHeader("Some-Header", "value");
        return "Chatbot response";
    }
}