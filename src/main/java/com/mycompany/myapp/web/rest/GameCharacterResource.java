package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.repository.GameCharacterRepository;
import com.mycompany.myapp.service.GameCharacterService;
import com.mycompany.myapp.service.dto.GameCharacterDTO;
import com.mycompany.myapp.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.reactive.ResponseUtil;

/**
 * REST controller for managing {@link com.mycompany.myapp.domain.GameCharacter}.
 */
@RestController
@RequestMapping("/api")
public class GameCharacterResource {

    private final Logger log = LoggerFactory.getLogger(GameCharacterResource.class);

    private static final String ENTITY_NAME = "gameCharacter";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final GameCharacterService gameCharacterService;

    private final GameCharacterRepository gameCharacterRepository;

    public GameCharacterResource(GameCharacterService gameCharacterService, GameCharacterRepository gameCharacterRepository) {
        this.gameCharacterService = gameCharacterService;
        this.gameCharacterRepository = gameCharacterRepository;
    }

    /**
     * {@code POST  /game-characters} : Create a new gameCharacter.
     *
     * @param gameCharacterDTO the gameCharacterDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new gameCharacterDTO, or with status {@code 400 (Bad Request)} if the gameCharacter has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/game-characters")
    public Mono<ResponseEntity<GameCharacterDTO>> createGameCharacter(@Valid @RequestBody GameCharacterDTO gameCharacterDTO)
        throws URISyntaxException {
        log.debug("REST request to save GameCharacter : {}", gameCharacterDTO);
        if (gameCharacterDTO.getId() != null) {
            throw new BadRequestAlertException("A new gameCharacter cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return gameCharacterService
            .save(gameCharacterDTO)
            .map(result -> {
                try {
                    return ResponseEntity
                        .created(new URI("/api/game-characters/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /game-characters/:id} : Updates an existing gameCharacter.
     *
     * @param id the id of the gameCharacterDTO to save.
     * @param gameCharacterDTO the gameCharacterDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated gameCharacterDTO,
     * or with status {@code 400 (Bad Request)} if the gameCharacterDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the gameCharacterDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/game-characters/{id}")
    public Mono<ResponseEntity<GameCharacterDTO>> updateGameCharacter(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody GameCharacterDTO gameCharacterDTO
    ) throws URISyntaxException {
        log.debug("REST request to update GameCharacter : {}, {}", id, gameCharacterDTO);
        if (gameCharacterDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, gameCharacterDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return gameCharacterRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return gameCharacterService
                    .update(gameCharacterDTO)
                    .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)))
                    .map(result ->
                        ResponseEntity
                            .ok()
                            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
                            .body(result)
                    );
            });
    }

    /**
     * {@code PATCH  /game-characters/:id} : Partial updates given fields of an existing gameCharacter, field will ignore if it is null
     *
     * @param id the id of the gameCharacterDTO to save.
     * @param gameCharacterDTO the gameCharacterDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated gameCharacterDTO,
     * or with status {@code 400 (Bad Request)} if the gameCharacterDTO is not valid,
     * or with status {@code 404 (Not Found)} if the gameCharacterDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the gameCharacterDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/game-characters/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<GameCharacterDTO>> partialUpdateGameCharacter(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody GameCharacterDTO gameCharacterDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update GameCharacter partially : {}, {}", id, gameCharacterDTO);
        if (gameCharacterDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, gameCharacterDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return gameCharacterRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<GameCharacterDTO> result = gameCharacterService.partialUpdate(gameCharacterDTO);

                return result
                    .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)))
                    .map(res ->
                        ResponseEntity
                            .ok()
                            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, res.getId().toString()))
                            .body(res)
                    );
            });
    }

    /**
     * {@code GET  /game-characters} : get all the gameCharacters.
     *
     * @param pageable the pagination information.
     * @param request a {@link ServerHttpRequest} request.
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of gameCharacters in body.
     */
    @GetMapping("/game-characters")
    public Mono<ResponseEntity<List<GameCharacterDTO>>> getAllGameCharacters(
        @org.springdoc.api.annotations.ParameterObject Pageable pageable,
        ServerHttpRequest request,
        @RequestParam(required = false, defaultValue = "false") boolean eagerload
    ) {
        log.debug("REST request to get a page of GameCharacters");
        return gameCharacterService
            .countAll()
            .zipWith(gameCharacterService.findAll(pageable).collectList())
            .map(countWithEntities ->
                ResponseEntity
                    .ok()
                    .headers(
                        PaginationUtil.generatePaginationHttpHeaders(
                            UriComponentsBuilder.fromHttpRequest(request),
                            new PageImpl<>(countWithEntities.getT2(), pageable, countWithEntities.getT1())
                        )
                    )
                    .body(countWithEntities.getT2())
            );
    }

    @GetMapping("/game-characters/{identifier}")
    public Mono<ResponseEntity<GameCharacterDTO>> getGameCharacter(@PathVariable String identifier) {
        log.debug("REST request to get GameCharacter by identifier: {}", identifier);

        if (StringUtils.isNumeric(identifier)) {
            // If the identifier is numeric, treat it as an ID
            Long id = Long.parseLong(identifier);
            Mono<GameCharacterDTO> gameCharacterDTO = gameCharacterService.findOne(id);
            return ResponseUtil.wrapOrNotFound(gameCharacterDTO);
        } else {
            Mono<GameCharacterDTO> gameCharacterDTO = gameCharacterService.findByUniqueLink(identifier);
            return ResponseUtil.wrapOrNotFound(gameCharacterDTO);
        }
    }

    @GetMapping("/game-characters/unique-link/{link}")
    public Mono<ResponseEntity<Boolean>> checkUniqueLink(@PathVariable String link) {
        Mono<Boolean> isUnique = gameCharacterService.isLinkUnique(link);
        return isUnique.map(unique -> ResponseEntity.ok().body(unique)).defaultIfEmpty(ResponseEntity.notFound().build());
    }

    /**
     * {@code DELETE  /game-characters/:id} : delete the "id" gameCharacter.
     *
     * @param id the id of the gameCharacterDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/game-characters/{id}")
    public Mono<ResponseEntity<Void>> deleteGameCharacter(@PathVariable Long id) {
        log.debug("REST request to delete GameCharacter : {}", id);
        return gameCharacterService
            .delete(id)
            .then(
                Mono.just(
                    ResponseEntity
                        .noContent()
                        .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
                        .build()
                )
            );
    }
}
