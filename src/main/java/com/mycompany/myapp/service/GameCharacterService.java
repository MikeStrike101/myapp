package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.GameCharacter;
import com.mycompany.myapp.repository.GameCharacterRepository;
import com.mycompany.myapp.service.dto.GameCharacterDTO;
import com.mycompany.myapp.service.mapper.GameCharacterMapper;
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

    public GameCharacterService(GameCharacterRepository gameCharacterRepository, GameCharacterMapper gameCharacterMapper) {
        this.gameCharacterRepository = gameCharacterRepository;
        this.gameCharacterMapper = gameCharacterMapper;
    }

    /**
     * Save a gameCharacter.
     *
     * @param gameCharacterDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<GameCharacterDTO> save(GameCharacterDTO gameCharacterDTO) {
        log.debug("Request to save GameCharacter : {}", gameCharacterDTO);
        return gameCharacterRepository.save(gameCharacterMapper.toEntity(gameCharacterDTO)).map(gameCharacterMapper::toDto);
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
        return gameCharacterRepository.findById(id).map(gameCharacterMapper::toDto);
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
}
