package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.repository.UserProblemRepository;
import com.mycompany.myapp.service.UserProblemService;
import com.mycompany.myapp.service.dto.UserProblemDTO;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.UserProblem}.
 */
@RestController
@RequestMapping("/api")
public class UserProblemResource {

    private final Logger log = LoggerFactory.getLogger(UserProblemResource.class);

    private static final String ENTITY_NAME = "userProblem";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final UserProblemService userProblemService;

    private final UserProblemRepository userProblemRepository;

    public UserProblemResource(UserProblemService userProblemService, UserProblemRepository userProblemRepository) {
        this.userProblemService = userProblemService;
        this.userProblemRepository = userProblemRepository;
    }

    /**
     * {@code POST  /user-problems} : Create a new userProblem.
     *
     * @param userProblemDTO the userProblemDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new userProblemDTO, or with status {@code 400 (Bad Request)} if the userProblem has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/user-problems")
    public Mono<ResponseEntity<UserProblemDTO>> createUserProblem(@Valid @RequestBody UserProblemDTO userProblemDTO)
        throws URISyntaxException {
        log.debug("REST request to save UserProblem : {}", userProblemDTO);
        if (userProblemDTO.getId() != null) {
            throw new BadRequestAlertException("A new userProblem cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return userProblemService
            .save(userProblemDTO)
            .map(result -> {
                try {
                    return ResponseEntity
                        .created(new URI("/api/user-problems/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /user-problems/:id} : Updates an existing userProblem.
     *
     * @param id the id of the userProblemDTO to save.
     * @param userProblemDTO the userProblemDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated userProblemDTO,
     * or with status {@code 400 (Bad Request)} if the userProblemDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the userProblemDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/user-problems/{id}")
    public Mono<ResponseEntity<UserProblemDTO>> updateUserProblem(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody UserProblemDTO userProblemDTO
    ) throws URISyntaxException {
        log.debug("REST request to update UserProblem : {}, {}", id, userProblemDTO);
        if (userProblemDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, userProblemDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return userProblemRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return userProblemService
                    .update(userProblemDTO)
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
     * {@code PATCH  /user-problems/:id} : Partial updates given fields of an existing userProblem, field will ignore if it is null
     *
     * @param id the id of the userProblemDTO to save.
     * @param userProblemDTO the userProblemDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated userProblemDTO,
     * or with status {@code 400 (Bad Request)} if the userProblemDTO is not valid,
     * or with status {@code 404 (Not Found)} if the userProblemDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the userProblemDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/user-problems/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<UserProblemDTO>> partialUpdateUserProblem(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody UserProblemDTO userProblemDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update UserProblem partially : {}, {}", id, userProblemDTO);
        if (userProblemDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, userProblemDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return userProblemRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<UserProblemDTO> result = userProblemService.partialUpdate(userProblemDTO);

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
     * {@code GET  /user-problems} : get all the userProblems.
     *
     * @param pageable the pagination information.
     * @param request a {@link ServerHttpRequest} request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of userProblems in body.
     */
    @GetMapping("/user-problems")
    public Mono<ResponseEntity<List<UserProblemDTO>>> getAllUserProblems(
        @org.springdoc.api.annotations.ParameterObject Pageable pageable,
        ServerHttpRequest request
    ) {
        log.debug("REST request to get a page of UserProblems");
        return userProblemService
            .countAll()
            .zipWith(userProblemService.findAll(pageable).collectList())
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
     * {@code GET  /user-problems/:id} : get the "id" userProblem.
     *
     * @param id the id of the userProblemDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the userProblemDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/user-problems/{id}")
    public Mono<ResponseEntity<UserProblemDTO>> getUserProblem(@PathVariable Long id) {
        log.debug("REST request to get UserProblem : {}", id);
        Mono<UserProblemDTO> userProblemDTO = userProblemService.findOne(id);
        return ResponseUtil.wrapOrNotFound(userProblemDTO);
    }

    /**
     * {@code DELETE  /user-problems/:id} : delete the "id" userProblem.
     *
     * @param id the id of the userProblemDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/user-problems/{id}")
    public Mono<ResponseEntity<Void>> deleteUserProblem(@PathVariable Long id) {
        log.debug("REST request to delete UserProblem : {}", id);
        return userProblemService
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
