package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.repository.ExecutionCodeRepository;
import com.mycompany.myapp.service.ExecutionCodeService;
import com.mycompany.myapp.service.dto.ExecutionCodeDTO;
import com.mycompany.myapp.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.reactive.ResponseUtil;

/**
 * REST controller for managing {@link com.mycompany.myapp.domain.ExecutionCode}.
 */
@RestController
@RequestMapping("/api")
public class ExecutionCodeResource {

    private final Logger log = LoggerFactory.getLogger(ExecutionCodeResource.class);

    private static final String ENTITY_NAME = "executionCode";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ExecutionCodeService executionCodeService;

    private final ExecutionCodeRepository executionCodeRepository;

    public ExecutionCodeResource(ExecutionCodeService executionCodeService, ExecutionCodeRepository executionCodeRepository) {
        this.executionCodeService = executionCodeService;
        this.executionCodeRepository = executionCodeRepository;
    }

    /**
     * {@code POST  /execution-codes} : Create a new executionCode.
     *
     * @param executionCodeDTO the executionCodeDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new executionCodeDTO, or with status {@code 400 (Bad Request)} if the executionCode has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/execution-codes")
    public Mono<ResponseEntity<ExecutionCodeDTO>> createExecutionCode(@Valid @RequestBody ExecutionCodeDTO executionCodeDTO)
        throws URISyntaxException {
        log.debug("REST request to save ExecutionCode : {}", executionCodeDTO);
        if (executionCodeDTO.getId() != null) {
            throw new BadRequestAlertException("A new executionCode cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return executionCodeService
            .save(executionCodeDTO)
            .map(result -> {
                try {
                    return ResponseEntity
                        .created(new URI("/api/execution-codes/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /execution-codes/:id} : Updates an existing executionCode.
     *
     * @param id the id of the executionCodeDTO to save.
     * @param executionCodeDTO the executionCodeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated executionCodeDTO,
     * or with status {@code 400 (Bad Request)} if the executionCodeDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the executionCodeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/execution-codes/{id}")
    public Mono<ResponseEntity<ExecutionCodeDTO>> updateExecutionCode(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ExecutionCodeDTO executionCodeDTO
    ) throws URISyntaxException {
        log.debug("REST request to update ExecutionCode : {}, {}", id, executionCodeDTO);
        if (executionCodeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, executionCodeDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return executionCodeRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return executionCodeService
                    .update(executionCodeDTO)
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
     * {@code PATCH  /execution-codes/:id} : Partial updates given fields of an existing executionCode, field will ignore if it is null
     *
     * @param id the id of the executionCodeDTO to save.
     * @param executionCodeDTO the executionCodeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated executionCodeDTO,
     * or with status {@code 400 (Bad Request)} if the executionCodeDTO is not valid,
     * or with status {@code 404 (Not Found)} if the executionCodeDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the executionCodeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/execution-codes/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<ExecutionCodeDTO>> partialUpdateExecutionCode(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ExecutionCodeDTO executionCodeDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update ExecutionCode partially : {}, {}", id, executionCodeDTO);
        if (executionCodeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, executionCodeDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return executionCodeRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<ExecutionCodeDTO> result = executionCodeService.partialUpdate(executionCodeDTO);

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
     * {@code GET  /execution-codes} : get all the executionCodes.
     *
     * @param pageable the pagination information.
     * @param request a {@link ServerHttpRequest} request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of executionCodes in body.
     */
    @GetMapping("/execution-codes")
    public Mono<ResponseEntity<List<ExecutionCodeDTO>>> getAllExecutionCodes(
        @org.springdoc.api.annotations.ParameterObject Pageable pageable,
        ServerHttpRequest request
    ) {
        log.debug("REST request to get a page of ExecutionCodes");
        return executionCodeService
            .countAll()
            .zipWith(executionCodeService.findAll(pageable).collectList())
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

    @GetMapping("/execution-codes/by-character/{gameCharacterId}")
    public Mono<ResponseEntity<List<ExecutionCodeDTO>>> getExecutionCodesByGameCharacter(@PathVariable Long gameCharacterId) {
        log.debug("REST request to get ExecutionCodes by gameCharacterId : {}", gameCharacterId);
        return executionCodeService
            .findByGameCharacterId(gameCharacterId)
            .collectList()
            .map(executionCodeDTOs -> {
                if (executionCodeDTOs.isEmpty()) {
                    return ResponseEntity.notFound().build();
                } else {
                    return ResponseEntity.ok().body(executionCodeDTOs);
                }
            });
    }

    /**
     * {@code GET  /execution-codes/:id} : get the "id" executionCode.
     *
     * @param id the id of the executionCodeDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the executionCodeDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/execution-codes/{id}")
    public Mono<ResponseEntity<ExecutionCodeDTO>> getExecutionCode(@PathVariable Long id) {
        log.debug("REST request to get ExecutionCode : {}", id);
        Mono<ExecutionCodeDTO> executionCodeDTO = executionCodeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(executionCodeDTO);
    }

    /**
     * {@code DELETE  /execution-codes/:id} : delete the "id" executionCode.
     *
     * @param id the id of the executionCodeDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/execution-codes/{id}")
    public Mono<ResponseEntity<Void>> deleteExecutionCode(@PathVariable Long id) {
        log.debug("REST request to delete ExecutionCode : {}", id);
        return executionCodeService
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
