package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.repository.ProblemRepository;
import com.mycompany.myapp.service.ProblemService;
import com.mycompany.myapp.service.dto.ProblemDTO;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.Problem}.
 */
@RestController
@RequestMapping("/api")
public class ProblemResource {

    private final Logger log = LoggerFactory.getLogger(ProblemResource.class);

    private static final String ENTITY_NAME = "problem";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ProblemService problemService;

    private final ProblemRepository problemRepository;

    public ProblemResource(ProblemService problemService, ProblemRepository problemRepository) {
        this.problemService = problemService;
        this.problemRepository = problemRepository;
    }

    /**
     * {@code POST  /problems} : Create a new problem.
     *
     * @param problemDTO the problemDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new problemDTO, or with status {@code 400 (Bad Request)} if the problem has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/problems")
    public Mono<ResponseEntity<ProblemDTO>> createProblem(@Valid @RequestBody ProblemDTO problemDTO) throws URISyntaxException {
        log.debug("REST request to save Problem : {}", problemDTO);
        if (problemDTO.getId() != null) {
            throw new BadRequestAlertException("A new problem cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return problemService
            .save(problemDTO)
            .map(result -> {
                try {
                    return ResponseEntity
                        .created(new URI("/api/problems/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /problems/:id} : Updates an existing problem.
     *
     * @param id the id of the problemDTO to save.
     * @param problemDTO the problemDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated problemDTO,
     * or with status {@code 400 (Bad Request)} if the problemDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the problemDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/problems/{id}")
    public Mono<ResponseEntity<ProblemDTO>> updateProblem(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ProblemDTO problemDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Problem : {}, {}", id, problemDTO);
        if (problemDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, problemDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return problemRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return problemService
                    .update(problemDTO)
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
     * {@code PATCH  /problems/:id} : Partial updates given fields of an existing problem, field will ignore if it is null
     *
     * @param id the id of the problemDTO to save.
     * @param problemDTO the problemDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated problemDTO,
     * or with status {@code 400 (Bad Request)} if the problemDTO is not valid,
     * or with status {@code 404 (Not Found)} if the problemDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the problemDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/problems/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<ProblemDTO>> partialUpdateProblem(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ProblemDTO problemDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Problem partially : {}, {}", id, problemDTO);
        if (problemDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, problemDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return problemRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<ProblemDTO> result = problemService.partialUpdate(problemDTO);

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
     * {@code GET  /problems} : get all the problems.
     *
     * @param pageable the pagination information.
     * @param request a {@link ServerHttpRequest} request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of problems in body.
     */
    @GetMapping("/problems")
    public Mono<ResponseEntity<List<ProblemDTO>>> getAllProblems(
        @org.springdoc.api.annotations.ParameterObject Pageable pageable,
        ServerHttpRequest request
    ) {
        log.debug("REST request to get a page of Problems");
        return problemService
            .countAll()
            .zipWith(problemService.findAll(pageable).collectList())
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

    /**
     * {@code GET  /problems/:id} : get the "id" problem.
     *
     * @param id the id of the problemDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the problemDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/problems/{id}")
    public Mono<ResponseEntity<ProblemDTO>> getProblem(@PathVariable Long id) {
        log.debug("REST request to get Problem : {}", id);
        Mono<ProblemDTO> problemDTO = problemService.findOne(id);
        return ResponseUtil.wrapOrNotFound(problemDTO);
    }

    /**
     * {@code DELETE  /problems/:id} : delete the "id" problem.
     *
     * @param id the id of the problemDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/problems/{id}")
    public Mono<ResponseEntity<Void>> deleteProblem(@PathVariable Long id) {
        log.debug("REST request to delete Problem : {}", id);
        return problemService
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
