package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.UserProblem;
import com.mycompany.myapp.repository.UserProblemRepository;
import com.mycompany.myapp.service.dto.UserProblemDTO;
import com.mycompany.myapp.service.mapper.UserProblemMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link UserProblem}.
 */
@Service
@Transactional
public class UserProblemService {

    private final Logger log = LoggerFactory.getLogger(UserProblemService.class);

    private final UserProblemRepository userProblemRepository;

    private final UserProblemMapper userProblemMapper;

    public UserProblemService(UserProblemRepository userProblemRepository, UserProblemMapper userProblemMapper) {
        this.userProblemRepository = userProblemRepository;
        this.userProblemMapper = userProblemMapper;
    }

    /**
     * Save a userProblem.
     *
     * @param userProblemDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<UserProblemDTO> save(UserProblemDTO userProblemDTO) {
        log.debug("Request to save UserProblem : {}", userProblemDTO);
        return userProblemRepository.save(userProblemMapper.toEntity(userProblemDTO)).map(userProblemMapper::toDto);
    }

    /**
     * Update a userProblem.
     *
     * @param userProblemDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<UserProblemDTO> update(UserProblemDTO userProblemDTO) {
        log.debug("Request to update UserProblem : {}", userProblemDTO);
        return userProblemRepository.save(userProblemMapper.toEntity(userProblemDTO)).map(userProblemMapper::toDto);
    }

    /**
     * Partially update a userProblem.
     *
     * @param userProblemDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Mono<UserProblemDTO> partialUpdate(UserProblemDTO userProblemDTO) {
        log.debug("Request to partially update UserProblem : {}", userProblemDTO);

        return userProblemRepository
            .findById(userProblemDTO.getId())
            .map(existingUserProblem -> {
                userProblemMapper.partialUpdate(existingUserProblem, userProblemDTO);

                return existingUserProblem;
            })
            .flatMap(userProblemRepository::save)
            .map(userProblemMapper::toDto);
    }

    /**
     * Get all the userProblems.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Flux<UserProblemDTO> findAll(Pageable pageable) {
        log.debug("Request to get all UserProblems");
        return userProblemRepository.findAllBy(pageable).map(userProblemMapper::toDto);
    }

    /**
     * Returns the number of userProblems available.
     * @return the number of entities in the database.
     *
     */
    public Mono<Long> countAll() {
        return userProblemRepository.count();
    }

    /**
     * Get one userProblem by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Mono<UserProblemDTO> findOne(Long id) {
        log.debug("Request to get UserProblem : {}", id);
        return userProblemRepository.findById(id).map(userProblemMapper::toDto);
    }

    /**
     * Delete the userProblem by id.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    public Mono<Void> delete(Long id) {
        log.debug("Request to delete UserProblem : {}", id);
        return userProblemRepository.deleteById(id);
    }
}
