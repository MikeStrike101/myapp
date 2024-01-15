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
/*import org.springframework.http.HttpHeaders;
import java.util.List;
import java.util.Map;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/api")
public class CodeExecutionResource {

    private final RestTemplate restTemplate;

    public CodeExecutionResource(RestTemplateBuilder restTemplateBuilder) {
        this.restTemplate = restTemplateBuilder.build();
    }

    @PostMapping("/execute-code")
    public ResponseEntity<String> executeCode(@RequestBody Map<String, Object> payload) {
        String code = (String) payload.get("code");
        String language = (String) payload.get("language"); 
        String pistonApiUrl = "https://emkc.org/api/v1/piston/execute";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, Object> request = Map.of(
                "language", "python", // or whatever language is needed
                "version", "3.8", // specify the version
                "files", List.of(Map.of( "language", language,
                                        "source", code))
        );

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
HttpEntity<Map<String, Object>> entity = new HttpEntity<>(pistonRequestBody, headers);
String pistonApiUrl = "https://emkc.org/api/v1/piston/execute";

try {
    // Send the request to the Piston API
    ResponseEntity<String> response = restTemplate.postForEntity(pistonApiUrl, entity, String.class);
    // Return the response from the Piston API
    return ResponseEntity.ok(response.getBody());
} catch (HttpClientErrorException e) {
    // Handle HTTP errors here
    return ResponseEntity.status(e.getStatusCode()).body(e.getResponseBodyAsString());
} catch (Exception e) {
    // Handle other errors here
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error executing code");
}
    }
}
*/
