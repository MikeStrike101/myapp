package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.service.GameCharacterService;
import com.mycompany.myapp.service.ProblemService;
import com.mycompany.myapp.service.ProgressService;
import com.mycompany.myapp.service.dto.GameCharacterDTO;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api")
public class GenerateImageResource {

    private final GameCharacterService gameCharacterService;

    private final ProgressService progressService;

    private final ProblemService problemService;

    private final Logger log = LoggerFactory.getLogger(GenerateImageResource.class);

    public GenerateImageResource(
        GameCharacterService gameCharacterService,
        ProgressService progressService,
        ProblemService problemService
    ) {
        this.gameCharacterService = gameCharacterService;
        this.progressService = progressService;
        this.problemService = problemService;
    }

    @PostMapping("/generate-new-image")
    public Mono<ResponseEntity<Map<String, Object>>> generateCharacterImage(@RequestBody GameCharacterDTO gameCharacterDTO) {
        log.debug("Request to generate image for game character: {}", gameCharacterDTO);

        return gameCharacterService
            .generateAndSaveImage(gameCharacterDTO)
            .flatMap(imagePath -> {
                gameCharacterDTO.setProfilePicture(imagePath);
                log.debug("Image generated successfully for game character: {}", gameCharacterDTO);

                Map<String, Object> response = new HashMap<>();
                response.put("message", "Image generated successfully");
                response.put("imagePath", imagePath);
                return Mono.just(ResponseEntity.ok().body(response));
            })
            .onErrorResume(e -> {
                log.error("Failed to generate image for game character: {}", gameCharacterDTO, e);
                return Mono.just(
                    ResponseEntity
                        .status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(Collections.singletonMap("error", "Failed to generate image: " + e.getMessage()))
                );
            });
    }
}
