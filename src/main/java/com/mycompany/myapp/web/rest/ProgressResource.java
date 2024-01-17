package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.repository.ProgressRepository;
import com.mycompany.myapp.service.ProgressService;
import com.mycompany.myapp.service.dto.ProgressDTO;
import com.mycompany.myapp.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.reactive.ResponseUtil;

/**
 * REST controller for managing {@link com.mycompany.myapp.domain.Progress}.
 */
@RestController
@RequestMapping("/api")
public class ProgressResource {

    private final Logger log = LoggerFactory.getLogger(ProgressResource.class);

    private static final String ENTITY_NAME = "progress";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ProgressService progressService;

    private final ProgressRepository progressRepository;

    public ProgressResource(ProgressService progressService, ProgressRepository progressRepository) {
        this.progressService = progressService;
        this.progressRepository = progressRepository;
    }

    /**
     * {@code POST  /progresses} : Create a new progress.
     *
     * @param progressDTO the progressDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new progressDTO, or with status {@code 400 (Bad Request)} if the progress has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/progresses")
    public Mono<ResponseEntity<ProgressDTO>> createProgress(@Valid @RequestBody ProgressDTO progressDTO) throws URISyntaxException {
        log.debug("REST request to save Progress : {}", progressDTO);
        if (progressDTO.getId() != null) {
            throw new BadRequestAlertException("A new progress cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return progressService
            .save(progressDTO)
            .map(result -> {
                try {
                    return ResponseEntity
                        .created(new URI("/api/progresses/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    @GetMapping("/progress/{uniqueLinkId}")
    public ResponseEntity<?> getCurrentQuestion(@PathVariable String uniqueLinkId) {
        try {
            Integer currentQuestion = progressService.getCurrentQuestion(uniqueLinkId);
            if (currentQuestion != null) {
                return ResponseEntity.ok(currentQuestion);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Current question not found for the given unique link.");
            }
        } catch (Exception e) {
            // Log the exception details as well for debugging
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while retrieving the current question.");
        }
    }

    /**
     * {@code PUT  /progresses/:id} : Updates an existing progress.
     *
     * @param id the id of the progressDTO to save.
     * @param progressDTO the progressDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated progressDTO,
     * or with status {@code 400 (Bad Request)} if the progressDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the progressDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/progresses/{id}")
    public Mono<ResponseEntity<ProgressDTO>> updateProgress(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ProgressDTO progressDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Progress : {}, {}", id, progressDTO);
        if (progressDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, progressDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return progressRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return progressService
                    .update(progressDTO)
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
     * {@code PATCH  /progresses/:id} : Partial updates given fields of an existing progress, field will ignore if it is null
     *
     * @param id the id of the progressDTO to save.
     * @param progressDTO the progressDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated progressDTO,
     * or with status {@code 400 (Bad Request)} if the progressDTO is not valid,
     * or with status {@code 404 (Not Found)} if the progressDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the progressDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/progresses/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<ProgressDTO>> partialUpdateProgress(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ProgressDTO progressDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Progress partially : {}, {}", id, progressDTO);
        if (progressDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, progressDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return progressRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<ProgressDTO> result = progressService.partialUpdate(progressDTO);

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
     * {@code GET  /progresses} : get all the progresses.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of progresses in body.
     */
    @GetMapping("/progresses")
    public Mono<List<ProgressDTO>> getAllProgresses() {
        log.debug("REST request to get all Progresses");
        return progressService.findAll().collectList();
    }

    /**
     * {@code GET  /progresses} : get all the progresses as a stream.
     * @return the {@link Flux} of progresses.
     */
    @GetMapping(value = "/progresses", produces = MediaType.APPLICATION_NDJSON_VALUE)
    public Flux<ProgressDTO> getAllProgressesAsStream() {
        log.debug("REST request to get all Progresses as a stream");
        return progressService.findAll();
    }

    /**
     * {@code GET  /progresses/:id} : get the "id" progress.
     *
     * @param id the id of the progressDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the progressDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/progresses/{id}")
    public Mono<ResponseEntity<ProgressDTO>> getProgress(@PathVariable Long id) {
        log.debug("REST request to get Progress : {}", id);
        Mono<ProgressDTO> progressDTO = progressService.findOne(id);
        return ResponseUtil.wrapOrNotFound(progressDTO);
    }

    /**
     * {@code DELETE  /progresses/:id} : delete the "id" progress.
     *
     * @param id the id of the progressDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/progresses/{id}")
    public Mono<ResponseEntity<Void>> deleteProgress(@PathVariable Long id) {
        log.debug("REST request to delete Progress : {}", id);
        return progressService
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
