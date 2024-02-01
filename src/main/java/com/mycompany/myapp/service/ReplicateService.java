package com.mycompany.myapp.service;

import com.mycompany.myapp.service.dto.ReplicateResponseDTO;
import java.time.Duration;
import java.util.Map;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class ReplicateService {

    private final WebClient webClient;
    private final String replicateApiToken = "r8_X8d6XiPdgD5KOo3KCiyDGdeWwTVPKsv4CGgan";

    public ReplicateService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("https://api.replicate.com").build();
    }

    public Mono<String> generateImage(String prompt) {
        return webClient
            .post()
            .uri("/v1/predictions")
            .header("Authorization", "Token " + replicateApiToken)
            .bodyValue(
                Map.of("version", "ac732df83cea7fff18b8472768c88ad041fa750ff7682a21affe81863cbe77e4", "input", Map.of("prompt", prompt))
            )
            .retrieve()
            .bodyToMono(ReplicateResponseDTO.class)
            .flatMap(response -> pollForCompletion(response.getId()));
    }

    public Mono<String> pollForCompletion(String taskId) {
        return webClient
            .get()
            .uri("/v1/predictions/" + taskId)
            .header("Authorization", "Token " + replicateApiToken)
            .retrieve()
            .bodyToMono(ReplicateResponseDTO.class)
            .flatMap(response -> {
                if ("succeeded".equals(response.getStatus())) {
                    return Mono.justOrEmpty(response.getFirstImageUrl());
                } else if ("starting".equals(response.getStatus()) || "processing".equals(response.getStatus())) {
                    // Delay and retry
                    return Mono.delay(Duration.ofSeconds(5)).then(pollForCompletion(taskId));
                } else {
                    // Handle other statuses (failed, cancelled, etc.)
                    return Mono.error(new RuntimeException("Task failed or cancelled"));
                }
            });
    }
}
