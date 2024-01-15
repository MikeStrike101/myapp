package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.GameCharacter;
import com.mycompany.myapp.repository.GameCharacterRepository;
import com.mycompany.myapp.repository.ProblemRepository;
import com.mycompany.myapp.repository.ProgressRepository;
import com.mycompany.myapp.service.EmailService;
import com.mycompany.myapp.service.dto.GameCharacterDTO;
import com.mycompany.myapp.service.mapper.GameCharacterMapper;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link GameCharacter}.
 */
@Service
@Transactional
public class GameCharacterService {

    private final Logger log = LoggerFactory.getLogger(GameCharacterService.class);

    private final GameCharacterRepository gameCharacterRepository;

    private final GameCharacterMapper gameCharacterMapper;

    private final EmailService emailService;

    private final ReplicateService replicateService;

    private final ProgressRepository progressRepository;

    private final ProblemRepository problemRepository;

    public GameCharacterService(
        GameCharacterRepository gameCharacterRepository,
        GameCharacterMapper gameCharacterMapper,
        EmailService emailService,
        ReplicateService replicateService,
        ProgressRepository progressRepository,
        ProblemRepository problemRepository
    ) {
        this.gameCharacterRepository = gameCharacterRepository;
        this.gameCharacterMapper = gameCharacterMapper;
        this.emailService = emailService;
        this.replicateService = replicateService;
        this.progressRepository = progressRepository;
        this.problemRepository = problemRepository;
    }

    /**
     * Save a gameCharacter.
     *
     * @param gameCharacterDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<GameCharacterDTO> save(GameCharacterDTO gameCharacterDTO) {
        return gameCharacterRepository
            .save(gameCharacterMapper.toEntity(gameCharacterDTO))
            .flatMap(savedGameCharacter -> {
                // Construct the prompt for the Replicate API
                String prompt =
                    "A newbie pixelated character for the EvoCode platform which has the shape of a " +
                    savedGameCharacter.getShape() +
                    " " +
                    savedGameCharacter.getColor() +
                    " with " +
                    savedGameCharacter.getAccessory();
                // Call the ReplicateService to generate the image
                return replicateService
                    .generateImage(prompt)
                    .flatMap(imageUrl -> {
                        try {
                            String savedImagePath = downloadAndSaveImage(imageUrl, "gameCharacter" + savedGameCharacter.getId());
                            File savedImageFile = new File(savedImagePath);
                            savedGameCharacter.setProfilePicture(savedImageFile.getName());
                        } catch (IOException e) {
                            log.error("Error saving image", e);
                        }

                        return gameCharacterRepository.save(savedGameCharacter);
                    });
            })
            .map(gameCharacterMapper::toDto)/* .doOnSuccess(character -> {
                // Send email with unique link after everything is saved
                String userEmail = character.getEmail();
                if (userEmail != null && !userEmail.isEmpty()) {
                    emailService.sendUniqueLinkEmail(userEmail, "Your Game Character Link is here: ", character.getUniqueLink());
                }
            })*/;
    }

    /**
     * Update a gameCharacter.
     *
     * @param gameCharacterDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<GameCharacterDTO> update(GameCharacterDTO gameCharacterDTO) {
        log.debug("Request to update GameCharacter : {}", gameCharacterDTO);
        return gameCharacterRepository.save(gameCharacterMapper.toEntity(gameCharacterDTO)).map(gameCharacterMapper::toDto);
    }

    /**
     * Partially update a gameCharacter.
     *
     * @param gameCharacterDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Mono<GameCharacterDTO> partialUpdate(GameCharacterDTO gameCharacterDTO) {
        log.debug("Request to partially update GameCharacter : {}", gameCharacterDTO);

        return gameCharacterRepository
            .findById(gameCharacterDTO.getId())
            .map(existingGameCharacter -> {
                gameCharacterMapper.partialUpdate(existingGameCharacter, gameCharacterDTO);

                return existingGameCharacter;
            })
            .flatMap(gameCharacterRepository::save)
            .map(gameCharacterMapper::toDto);
    }

    /**
     * Get all the gameCharacters.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Flux<GameCharacterDTO> findAll(Pageable pageable) {
        log.debug("Request to get all GameCharacters");
        return gameCharacterRepository.findAllBy(pageable).map(gameCharacterMapper::toDto);
    }

    /**
     * Get all the gameCharacters with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Flux<GameCharacterDTO> findAllWithEagerRelationships(Pageable pageable) {
        return gameCharacterRepository.findAllWithEagerRelationships(pageable).map(gameCharacterMapper::toDto);
    }

    /**
     * Returns the number of gameCharacters available.
     * @return the number of entities in the database.
     *
     */
    public Mono<Long> countAll() {
        return gameCharacterRepository.count();
    }

    /**
     * Get one gameCharacter by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Mono<GameCharacterDTO> findOne(Long id) {
        log.debug("Request to get GameCharacter : {}", id);
        return gameCharacterRepository.findOneWithEagerRelationships(id).map(gameCharacterMapper::toDto);
    }

    public Mono<GameCharacterDTO> findByUniqueLink(String uniqueLink) {
        log.debug("Request to get GameCharacter via uniqueLink : {}", uniqueLink);
        return gameCharacterRepository.findByUniqueLink(uniqueLink).map(gameCharacterMapper::toDto);
    }

    /**
     * Delete the gameCharacter by id.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    public Mono<Void> delete(Long id) {
        log.debug("Request to delete GameCharacter : {}", id);
        return gameCharacterRepository.deleteById(id);
    }

    private String downloadAndSaveImage(String imageUrl, String fileName) throws IOException {
        URL url = new URL(imageUrl);
        File targetDirectory = new File("src/main/webapp/content/images");
        if (!targetDirectory.exists()) {
            targetDirectory.mkdirs();
        }
        File outputFile = new File(targetDirectory, fileName + ".png");
        FileUtils.copyURLToFile(url, outputFile);
        return outputFile.getAbsolutePath();
    }
}
