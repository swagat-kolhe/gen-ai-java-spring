package com.genai.java.spring.config;

import com.openai.client.OpenAIClient;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.huggingface.HuggingfaceChatModel;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.ai.openaisdk.OpenAiSdkChatModel;
//import org.springframework.ai.vertexai.gemini.VertexAiGeminiChatModel;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AIProviderConfig {

    @Bean("openAIChatClient")
    ChatClient openAIChatClient(OpenAiSdkChatModel openAiSdkChatModel , SimpleLoggerAdvisor simpleLoggerAdvisor) {
        return ChatClient.builder(openAiSdkChatModel)
                .defaultAdvisors(simpleLoggerAdvisor)
                .build();
    }

//    @Bean("vertexAIChatClient")
//    ChatClient vertexAIChatClient(VertexAiGeminiChatModel vertexAiGeminiChatModel) {
//        return ChatClient.builder(vertexAiGeminiChatModel).build();
//    }

    @Bean("huggingFaceChatClient")
    ChatClient huggingFaceChatClient(HuggingfaceChatModel huggingfaceChatModel) {
        return ChatClient.builder(huggingfaceChatModel).build();
    }

    @Bean("ollamaChatClient")
    ChatClient ollamaChatClient(OllamaChatModel ollamaChatModel) {
        return ChatClient.builder(ollamaChatModel).build();
    }

    @Bean
    SimpleLoggerAdvisor simpleLoggerAdvisor() {
        return new SimpleLoggerAdvisor();
    }

}
