package com.mycompany.myapp.service;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class PistonService {

    private final WebClient webClient;

    public PistonService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("https://emkc.org/api/v2/piston").build();
    }

    public Mono<ExecutionResult> executeCode(String language, String version, String code) {
        // Construct the request payload
        ExecutionRequest payload = new ExecutionRequest(language, version, code);

        // Make the POST request to the Piston API
        return this.webClient.post().uri("/execute").bodyValue(payload).retrieve().bodyToMono(ExecutionResult.class);
    }

    // Inner class to match the expected request format of the Piston API
    private static class ExecutionRequest {

        private final String language;
        private final String version;
        private final String source;

        public ExecutionRequest(String language, String version, String source) {
            this.language = language;
            this.version = version;
            this.source = source;
        }
        // Getters (and setters if needed)
        // ...
    }

    // Inner class to capture the response from the Piston API
    public static class ExecutionResult {
        // Define fields according to the Piston API response structure
        // ...

        // Getters (and setters if needed)
        // ...
    }
}
