package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.Progress;
import com.mycompany.myapp.repository.ProgressRepository;
import com.mycompany.myapp.service.dto.ProgressDTO;
import com.mycompany.myapp.service.mapper.ProgressMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
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

    public ProgressService(ProgressRepository progressRepository, ProgressMapper progressMapper) {
        this.progressRepository = progressRepository;
        this.progressMapper = progressMapper;
    }

    public Integer getCurrentQuestion(String uniqueLinkId) {
        Optional<Progress> progress = progressRepository.findByUniqueLinkId(uniqueLinkId);
        return progress.map(Progress::getCurrentLesson).orElse(null);
    }

    /**
     * Save a progress.
     *
     * @param progressDTO the entity to save.
     * @return the persisted entity.
     */
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
}
