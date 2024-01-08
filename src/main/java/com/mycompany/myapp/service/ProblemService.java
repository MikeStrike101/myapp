package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.Problem;
import com.mycompany.myapp.repository.ProblemRepository;
import com.mycompany.myapp.service.dto.ProblemDTO;
import com.mycompany.myapp.service.mapper.ProblemMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link Problem}.
 */
@Service
@Transactional
public class ProblemService {

    private final Logger log = LoggerFactory.getLogger(ProblemService.class);

    private final ProblemRepository problemRepository;

    private final ProblemMapper problemMapper;

    public ProblemService(ProblemRepository problemRepository, ProblemMapper problemMapper) {
        this.problemRepository = problemRepository;
        this.problemMapper = problemMapper;
    }

    /**
     * Save a problem.
     *
     * @param problemDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<ProblemDTO> save(ProblemDTO problemDTO) {
        log.debug("Request to save Problem : {}", problemDTO);
        return problemRepository.save(problemMapper.toEntity(problemDTO)).map(problemMapper::toDto);
    }

    /**
     * Update a problem.
     *
     * @param problemDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<ProblemDTO> update(ProblemDTO problemDTO) {
        log.debug("Request to update Problem : {}", problemDTO);
        return problemRepository.save(problemMapper.toEntity(problemDTO)).map(problemMapper::toDto);
    }

    /**
     * Partially update a problem.
     *
     * @param problemDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Mono<ProblemDTO> partialUpdate(ProblemDTO problemDTO) {
        log.debug("Request to partially update Problem : {}", problemDTO);

        return problemRepository
            .findById(problemDTO.getId())
            .map(existingProblem -> {
                problemMapper.partialUpdate(existingProblem, problemDTO);

                return existingProblem;
            })
            .flatMap(problemRepository::save)
            .map(problemMapper::toDto);
    }

    /**
     * Get all the problems.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Flux<ProblemDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Problems");
        return problemRepository.findAllBy(pageable).map(problemMapper::toDto);
    }

    /**
     * Returns the number of problems available.
     * @return the number of entities in the database.
     *
     */
    public Mono<Long> countAll() {
        return problemRepository.count();
    }

    /**
     * Get one problem by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Mono<ProblemDTO> findOne(Long id) {
        log.debug("Request to get Problem : {}", id);
        return problemRepository.findById(id).map(problemMapper::toDto);
    }

    /**
     * Delete the problem by id.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    public Mono<Void> delete(Long id) {
        log.debug("Request to delete Problem : {}", id);
        return problemRepository.deleteById(id);
    }
}
