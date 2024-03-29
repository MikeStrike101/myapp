package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.repository.TestCaseRepository;
import com.mycompany.myapp.service.TestCaseService;
import com.mycompany.myapp.service.dto.TestCaseDTO;
import com.mycompany.myapp.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.TestCase}.
 */
@RestController
@RequestMapping("/api")
public class TestCaseResource {

    private final Logger log = LoggerFactory.getLogger(TestCaseResource.class);

    private static final String ENTITY_NAME = "testCase";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TestCaseService testCaseService;

    private final TestCaseRepository testCaseRepository;

    public TestCaseResource(TestCaseService testCaseService, TestCaseRepository testCaseRepository) {
        this.testCaseService = testCaseService;
        this.testCaseRepository = testCaseRepository;
    }

    /**
     * {@code POST  /test-cases} : Create a new testCase.
     *
     * @param testCaseDTO the testCaseDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new testCaseDTO, or with status {@code 400 (Bad Request)} if the testCase has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/test-cases")
    public Mono<ResponseEntity<TestCaseDTO>> createTestCase(@Valid @RequestBody TestCaseDTO testCaseDTO) throws URISyntaxException {
        log.debug("REST request to save TestCase : {}", testCaseDTO);
        if (testCaseDTO.getId() != null) {
            throw new BadRequestAlertException("A new testCase cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return testCaseService
            .save(testCaseDTO)
            .map(result -> {
                try {
                    return ResponseEntity
                        .created(new URI("/api/test-cases/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /test-cases/:id} : Updates an existing testCase.
     *
     * @param id the id of the testCaseDTO to save.
     * @param testCaseDTO the testCaseDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated testCaseDTO,
     * or with status {@code 400 (Bad Request)} if the testCaseDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the testCaseDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/test-cases/{id}")
    public Mono<ResponseEntity<TestCaseDTO>> updateTestCase(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody TestCaseDTO testCaseDTO
    ) throws URISyntaxException {
        log.debug("REST request to update TestCase : {}, {}", id, testCaseDTO);
        if (testCaseDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, testCaseDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return testCaseRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return testCaseService
                    .update(testCaseDTO)
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
     * {@code PATCH  /test-cases/:id} : Partial updates given fields of an existing testCase, field will ignore if it is null
     *
     * @param id the id of the testCaseDTO to save.
     * @param testCaseDTO the testCaseDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated testCaseDTO,
     * or with status {@code 400 (Bad Request)} if the testCaseDTO is not valid,
     * or with status {@code 404 (Not Found)} if the testCaseDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the testCaseDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/test-cases/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<TestCaseDTO>> partialUpdateTestCase(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody TestCaseDTO testCaseDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update TestCase partially : {}, {}", id, testCaseDTO);
        if (testCaseDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, testCaseDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return testCaseRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<TestCaseDTO> result = testCaseService.partialUpdate(testCaseDTO);

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
     * {@code GET  /test-cases} : get all the testCases.
     *
     * @param pageable the pagination information.
     * @param request a {@link ServerHttpRequest} request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of testCases in body.
     */
    @GetMapping("/test-cases")
    public Mono<ResponseEntity<List<TestCaseDTO>>> getAllTestCases(
        @org.springdoc.api.annotations.ParameterObject Pageable pageable,
        ServerHttpRequest request
    ) {
        log.debug("REST request to get a page of TestCases");
        return testCaseService
            .countAll()
            .zipWith(testCaseService.findAll(pageable).collectList())
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
     * {@code GET  /test-cases/:id} : get the "id" testCase.
     *
     * @param id the id of the testCaseDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the testCaseDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/test-cases/{id}")
    public Mono<ResponseEntity<TestCaseDTO>> getTestCase(@PathVariable Long id) {
        log.debug("REST request to get TestCase : {}", id);
        Mono<TestCaseDTO> testCaseDTO = testCaseService.findOne(id);
        return ResponseUtil.wrapOrNotFound(testCaseDTO);
    }

    /**
     * {@code DELETE  /test-cases/:id} : delete the "id" testCase.
     *
     * @param id the id of the testCaseDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/test-cases/{id}")
    public Mono<ResponseEntity<Void>> deleteTestCase(@PathVariable Long id) {
        log.debug("REST request to delete TestCase : {}", id);
        return testCaseService
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
