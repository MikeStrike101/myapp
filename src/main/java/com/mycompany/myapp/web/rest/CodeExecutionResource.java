package com.mycompany.myapp.web.rest;

import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api")
public class CodeExecutionResource {

    private final WebClient webClient;

    @Autowired
    public CodeExecutionResource(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("https://emkc.org").build();
    }

    @PostMapping("/execute-code")
    public Mono<ResponseEntity<String>> executeCode(@RequestBody Map<String, Object> payload) {
        String language = (String) payload.get("language");
        String version = (String) payload.get("version");
        List<Map<String, String>> files = (List<Map<String, String>>) payload.get("files");

        Map<String, Object> pistonRequestBody = Map.of("language", language, "version", version, "files", files);

        return webClient
            .post()
            .uri("/api/v2/piston/execute")
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(pistonRequestBody)
            .retrieve()
            .onStatus(
                HttpStatus::isError,
                response ->
                    response
                        .bodyToMono(String.class)
                        .flatMap(errorBody ->
                            Mono.error(new RuntimeException("Error executing code: " + response.statusCode() + " " + errorBody))
                        )
            )
            .toEntity(String.class);
    }
}
