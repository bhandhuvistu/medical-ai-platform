package com.example.medicalai.config;

import io.micrometer.observation.ObservationRegistry;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.model.tool.ToolCallingManager;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.ai.ollama.api.OllamaApi;
import org.springframework.ai.ollama.api.OllamaChatOptions;
import org.springframework.ai.ollama.management.ModelManagementOptions;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.web.client.ResponseErrorHandler;
import org.springframework.web.client.RestClient;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class AiConfig {

  @Bean
  public OllamaApi ollamaApi(
      ObjectProvider<RestClient.Builder> restClientBuilderProvider,
      ObjectProvider<WebClient.Builder> webClientBuilderProvider,
      ResponseErrorHandler responseErrorHandler,
      @Value("${spring.ai.ollama.base-url:http://localhost:11434}") String baseUrl) {
    return OllamaApi.builder()
        .baseUrl(baseUrl)
        .restClientBuilder(restClientBuilderProvider.getIfAvailable(RestClient::builder))
        .webClientBuilder(webClientBuilderProvider.getIfAvailable(WebClient::builder))
        .responseErrorHandler(responseErrorHandler)
        .build();
  }

  @Bean
  public ChatModel ollamaChatModel(
      OllamaApi ollamaApi,
      ObservationRegistry observationRegistry,
      @Value("${spring.ai.ollama.chat.options.model:llama3.1}") String model,
      @Value("${spring.ai.ollama.chat.options.temperature:0.7}") double temperature) {
    OllamaChatOptions options =
        OllamaChatOptions.builder().model(model).temperature(temperature).build();

    return OllamaChatModel.builder()
        .ollamaApi(ollamaApi)
        .defaultOptions(options)
        .toolCallingManager(ToolCallingManager.builder().build())
        .observationRegistry(observationRegistry)
        .modelManagementOptions(ModelManagementOptions.defaults())
        .build();
  }

  @Bean
  public ChatClient chatClient(ChatModel chatModel) {
    return ChatClient.create(chatModel);
  }

  @Bean
  public ObservationRegistry observationRegistry() {
    return ObservationRegistry.create();
  }

  @Bean
  public Jackson2ObjectMapperBuilder jackson2ObjectMapperBuilder() {
    return new Jackson2ObjectMapperBuilder();
  }
}
