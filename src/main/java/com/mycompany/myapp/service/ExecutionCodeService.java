package com.mycompany.myapp.service;

import com.google.common.base.Optional;
import com.mycompany.myapp.domain.ExecutionCode;
import com.mycompany.myapp.repository.ExecutionCodeRepository;
import com.mycompany.myapp.service.dto.ExecutionCodeDTO;
import com.mycompany.myapp.service.mapper.ExecutionCodeMapper;
import java.util.List;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link ExecutionCode}.
 */
@Service
@Transactional
public class ExecutionCodeService {

    private final Logger log = LoggerFactory.getLogger(ExecutionCodeService.class);

    private final ExecutionCodeRepository executionCodeRepository;

    private final ExecutionCodeMapper executionCodeMapper;

    public ExecutionCodeService(ExecutionCodeRepository executionCodeRepository, ExecutionCodeMapper executionCodeMapper) {
        this.executionCodeRepository = executionCodeRepository;
        this.executionCodeMapper = executionCodeMapper;
    }

    /**
     * Save a executionCode.
     *
     * @param executionCodeDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<ExecutionCodeDTO> save(ExecutionCodeDTO executionCodeDTO) {
        log.debug("Request to save ExecutionCode : {}", executionCodeDTO);
        return executionCodeRepository.save(executionCodeMapper.toEntity(executionCodeDTO)).map(executionCodeMapper::toDto);
    }

    /**
     * Update a executionCode.
     *
     * @param executionCodeDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<ExecutionCodeDTO> update(ExecutionCodeDTO executionCodeDTO) {
        log.debug("Request to update ExecutionCode : {}", executionCodeDTO);
        return executionCodeRepository.save(executionCodeMapper.toEntity(executionCodeDTO)).map(executionCodeMapper::toDto);
    }

    /**
     * Partially update a executionCode.
     *
     * @param executionCodeDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Mono<ExecutionCodeDTO> partialUpdate(ExecutionCodeDTO executionCodeDTO) {
        log.debug("Request to partially update ExecutionCode : {}", executionCodeDTO);

        return executionCodeRepository
            .findById(executionCodeDTO.getId())
            .map(existingExecutionCode -> {
                executionCodeMapper.partialUpdate(existingExecutionCode, executionCodeDTO);

                return existingExecutionCode;
            })
            .flatMap(executionCodeRepository::save)
            .map(executionCodeMapper::toDto);
    }

    /**
     * Get all the executionCodes.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Flux<ExecutionCodeDTO> findAll(Pageable pageable) {
        log.debug("Request to get all ExecutionCodes");
        return executionCodeRepository.findAllBy(pageable).map(executionCodeMapper::toDto);
    }

    /**
     * Returns the number of executionCodes available.
     * @return the number of entities in the database.
     *
     */
    public Mono<Long> countAll() {
        return executionCodeRepository.count();
    }

    /**
     * Get one executionCode by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Mono<ExecutionCodeDTO> findOne(Long id) {
        log.debug("Request to get ExecutionCode : {}", id);
        return executionCodeRepository.findById(id).map(executionCodeMapper::toDto);
    }

    /**
     * Delete the executionCode by id.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    public Mono<Void> delete(Long id) {
        log.debug("Request to delete ExecutionCode : {}", id);
        return executionCodeRepository.deleteById(id);
    }

    public Flux<ExecutionCodeDTO> findByGameCharacterId(Long gameCharacterId) {
        return executionCodeRepository.findByGameCharacterId(gameCharacterId).map(executionCodeMapper::toDto);
    }
}
