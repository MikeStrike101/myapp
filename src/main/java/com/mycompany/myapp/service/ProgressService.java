package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.GameCharacter;
import com.mycompany.myapp.domain.Progress;
import com.mycompany.myapp.repository.ProgressRepository;
import com.mycompany.myapp.service.dto.GameCharacterDTO;
import com.mycompany.myapp.service.dto.ProgressDTO;
import com.mycompany.myapp.service.dto.UpdateProgressRequestDTO;
import com.mycompany.myapp.service.mapper.GameCharacterMapper;
import com.mycompany.myapp.service.mapper.ProgressMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link Progress}.
 */
@Service
@Transactional
public class ProgressService {

    private final Logger log = LoggerFactory.getLogger(ProgressService.class);

    private final ProgressRepository progressRepository;

    private final ProgressMapper progressMapper;

    private final GameCharacterService gameCharacterService;

    private final GameCharacterMapper gameCharacterMapper;

    public ProgressService(
        ProgressRepository progressRepository,
        ProgressMapper progressMapper,
        GameCharacterService gameCharacterService,
        GameCharacterMapper gameCharacterMapper
    ) {
        this.progressRepository = progressRepository;
        this.progressMapper = progressMapper;
        this.gameCharacterService = gameCharacterService;
        this.gameCharacterMapper = gameCharacterMapper;
    }

    /**
     * Save a progress.
     *
     * @param progressDTO the entity to save.
     * @return the persisted entity.
     */
    @Transactional
    public Mono<ProgressDTO> save(ProgressDTO progressDTO) {
        log.debug("Request to save Progress : {}", progressDTO);
        return progressRepository.save(progressMapper.toEntity(progressDTO)).map(progressMapper::toDto);
    }

    /**
     * Update a progress.
     *
     * @param progressDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<ProgressDTO> update(ProgressDTO progressDTO) {
        log.debug("Request to update Progress : {}", progressDTO);
        return progressRepository.save(progressMapper.toEntity(progressDTO)).map(progressMapper::toDto);
    }

    /**
     * Partially update a progress.
     *
     * @param progressDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Mono<ProgressDTO> partialUpdate(ProgressDTO progressDTO) {
        log.debug("Request to partially update Progress : {}", progressDTO);

        return progressRepository
            .findById(progressDTO.getId())
            .map(existingProgress -> {
                progressMapper.partialUpdate(existingProgress, progressDTO);

                return existingProgress;
            })
            .flatMap(progressRepository::save)
            .map(progressMapper::toDto);
    }

    /**
     * Get all the progresses.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Flux<ProgressDTO> findAll() {
        log.debug("Request to get all Progresses");
        return progressRepository.findAll().map(progressMapper::toDto);
    }

    /**
     * Returns the number of progresses available.
     * @return the number of entities in the database.
     *
     */
    public Mono<Long> countAll() {
        return progressRepository.count();
    }

    /**
     * Get one progress by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Mono<ProgressDTO> findOne(Long id) {
        log.debug("Request to get Progress : {}", id);
        return progressRepository.findById(id).map(progressMapper::toDto);
    }

    /**
     * Delete the progress by id.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    public Mono<Void> delete(Long id) {
        log.debug("Request to delete Progress : {}", id);
        return progressRepository.deleteById(id);
    }

    public Mono<Progress> findByProgressId(Long progressId) {
        return progressRepository.findById(progressId);
    }

    public Mono<Void> updateUserProgress(UpdateProgressRequestDTO updateRequest) {
        Mono<Progress> progressUpdate = progressRepository
            .findById(updateRequest.getGameCharacterId())
            .flatMap(progress -> {
                progress.setCurrentLesson(updateRequest.getNextQuestionNumber());
                progress.setXp(updateRequest.getNewXP());
                return progressRepository.save(progress);
            });

        Mono<GameCharacterDTO> gameCharacterUpdate = gameCharacterService
            .findByGameCharacterId(updateRequest.getGameCharacterId())
            .flatMap(gameCharacter -> {
                GameCharacterDTO gameCharacterDTO = gameCharacterMapper.toDto(gameCharacter);
                gameCharacterDTO.setExperience(updateRequest.getNewXP());
                gameCharacterDTO.setLevel(updateRequest.getNewLevel());
                // No need to convert DTO back to entity, save the DTO directly
                return gameCharacterService.save(gameCharacterDTO);
            });

        return Mono.when(progressUpdate, gameCharacterUpdate).then();
    }
}
