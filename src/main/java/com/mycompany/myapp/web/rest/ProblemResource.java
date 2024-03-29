package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.GameCharacter;
import com.mycompany.myapp.repository.GameCharacterRepository;
import com.mycompany.myapp.repository.ProblemRepository;
import com.mycompany.myapp.service.ExecutionCodeService;
import com.mycompany.myapp.service.GameCharacterService;
import com.mycompany.myapp.service.PistonService;
import com.mycompany.myapp.service.ProblemService;
import com.mycompany.myapp.service.ProgressService;
import com.mycompany.myapp.service.TestCaseService;
import com.mycompany.myapp.service.dto.ExecutionCodeDTO;
import com.mycompany.myapp.service.dto.ProblemDTO;
import com.mycompany.myapp.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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

    private final PistonService pistonService;

    private final TestCaseService testCaseService;

    private final ProgressService progressService;

    private final ExecutionCodeService executionCodeService;

    private final GameCharacterService gameCharacterService;

    private final GameCharacterRepository gameCharacterRepository;

    public ProblemResource(
        ProblemService problemService,
        ProblemRepository problemRepository,
        PistonService pistonService,
        TestCaseService testCaseService,
        ProgressService progressService,
        ExecutionCodeService executionCodeService,
        GameCharacterService gameCharacterService,
        GameCharacterRepository gameCharacterRepository
    ) {
        this.problemService = problemService;
        this.problemRepository = problemRepository;
        this.pistonService = pistonService;
        this.testCaseService = testCaseService;
        this.progressService = progressService;
        this.executionCodeService = executionCodeService;
        this.gameCharacterService = gameCharacterService;
        this.gameCharacterRepository = gameCharacterRepository;
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

    @PostMapping("/submit-code")
    public Mono<ResponseEntity<Map<String, String>>> submitAndExecuteCode(@RequestBody ExecutionCodeDTO executionCodeDTO) {
        Mono<GameCharacter> gameCharacterMono = gameCharacterRepository.findById(executionCodeDTO.getGameCharacter());

        HashMap<String, String> programmingLanguageVersion = new HashMap<>();
        programmingLanguageVersion.put("python", "3.10");
        programmingLanguageVersion.put("java", "15.0.2");
        programmingLanguageVersion.put("javascript", "1.32.3");

        return gameCharacterMono.flatMap(gameCharacter -> {
            String language = gameCharacter.getProgrammingLanguage().toLowerCase();
            String version = programmingLanguageVersion.get(language);
            log.debug("Programming language and version {} {}", language, version);
            Map<String, String> file = Map.of("content", executionCodeDTO.getCode());
            List<Map<String, String>> files = List.of(file);
            log.debug("Received request to submit and execute code: {}", executionCodeDTO);

            return pistonService
                .executeCode(language, version, files)
                .flatMap(executionResult -> {
                    return testCaseService
                        .getExpectedOutputByQuestionId(executionCodeDTO.getQuestionNumber())
                        .flatMap(expectedOutput -> {
                            String actualOutputCleaned = executionResult.getOutput().replace("_", "").replaceAll("\\s+", "").trim();
                            String expectedOutputCleaned = expectedOutput.replace("_", "").replaceAll("\\s+", "").trim();

                            if (actualOutputCleaned.equals(expectedOutputCleaned)) {
                                return Mono.just(
                                    ResponseEntity.ok().body(Map.of("message", "Code executed successfully. Test case passed!"))
                                );
                            } else {
                                log.warn("Test case did not pass for question number: {}", executionCodeDTO.getQuestionNumber());
                                log.debug("Mismatch found:");
                                log.debug("Actual Output (Cleaned): {}", actualOutputCleaned);
                                log.debug("Expected Output (Cleaned): {}", expectedOutputCleaned);
                                return Mono.just(
                                    ResponseEntity.badRequest().body(Map.of("error", "Code execution failed. Test case did not pass."))
                                );
                            }
                        });
                })
                .onErrorResume(e -> {
                    log.error("Error during code execution", e);
                    return Mono.just(
                        ResponseEntity
                            .status(HttpStatus.INTERNAL_SERVER_ERROR)
                            .body(Map.of("error", "Error during code execution: " + e.getMessage()))
                    );
                });
        });
    }
}
