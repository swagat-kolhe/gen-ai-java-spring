package com.genai.java.spring.config;

import com.genai.java.spring.chat.advisor.ErrorWrappingAdvisor;
import com.genai.java.spring.chat.advisor.SystemPromptAdvisor;
import com.genai.java.spring.chat.advisor.ValidationAdvisor;
import com.openai.client.OpenAIClient;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.SafeGuardAdvisor;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.client.advisor.vectorstore.VectorStoreChatMemoryAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.huggingface.HuggingfaceChatModel;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.ai.openaisdk.OpenAiSdkChatModel;
//import org.springframework.ai.vertexai.gemini.VertexAiGeminiChatModel;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.ai.vectorstore.pgvector.PgVectorStore;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

@Configuration
public class AIProviderConfig {

    @Bean
    public PgVectorStore pgVectorStore(JdbcTemplate jdbcTemplate,
                                             @Qualifier("openAiEmbeddingModel") EmbeddingModel embeddingModel) {
        return PgVectorStore.builder(jdbcTemplate, embeddingModel)
                .initializeSchema(true)
                .build();
    }

    @Bean("openAIChatClient")
    ChatClient openAIChatClient(OpenAiSdkChatModel openAiSdkChatModel,
                                SimpleLoggerAdvisor simpleLoggerAdvisor,
                                SafeGuardAdvisor safeGuardAdvisor,
                                ErrorWrappingAdvisor errorWrappingAdvisor,
                                SystemPromptAdvisor systemPromptAdvisor,
                                ValidationAdvisor validationAdvisor) {
        return ChatClient.builder(openAiSdkChatModel)
                .defaultAdvisors(safeGuardAdvisor , simpleLoggerAdvisor , errorWrappingAdvisor , systemPromptAdvisor , validationAdvisor)
                .build();
    }

    @Bean("openAIChatClientWithMemory")
    ChatClient openAIChatClientWithMemory(OpenAiSdkChatModel openAiChatModel,
                                          ChatMemory chatMemory,
                                          PgVectorStore pgVectorStore) {
        return ChatClient.builder(openAiChatModel)
                //.defaultAdvisors(MessageChatMemoryAdvisor.builder(chatMemory).build())
                .defaultAdvisors(VectorStoreChatMemoryAdvisor.builder(pgVectorStore).build())
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

    @Bean
    SafeGuardAdvisor safeGuardAdvisor() {
        return new SafeGuardAdvisor(List.of(
                "password", "ssn", "credit card", "iban", "bank account",
                "api_key", "secret", "private_key", "token",
                "confidential", "classified", "internal only", "Ignore previous instructions",
                "Ignore instructions", "system prompt", "hack"));
    }

}
