package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static org.springframework.security.test.web.reactive.server.SecurityMockServerConfigurers.csrf;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.Problem;
import com.mycompany.myapp.repository.EntityManager;
import com.mycompany.myapp.repository.ProblemRepository;
import com.mycompany.myapp.service.dto.ProblemDTO;
import com.mycompany.myapp.service.mapper.ProblemMapper;
import java.time.Duration;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.reactive.server.WebTestClient;

/**
 * Integration tests for the {@link ProblemResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class ProblemResourceIT {

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAAAAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBBBBBBBBBBBB";

    private static final Integer DEFAULT_LEVEL = 1;
    private static final Integer UPDATED_LEVEL = 2;

    private static final Integer DEFAULT_XP_REWARD = 0;
    private static final Integer UPDATED_XP_REWARD = 1;

    private static final String ENTITY_API_URL = "/api/problems";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ProblemRepository problemRepository;

    @Autowired
    private ProblemMapper problemMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private Problem problem;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Problem createEntity(EntityManager em) {
        Problem problem = new Problem()
            .title(DEFAULT_TITLE)
            .description(DEFAULT_DESCRIPTION)
            .level(DEFAULT_LEVEL)
            .xpReward(DEFAULT_XP_REWARD);
        return problem;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Problem createUpdatedEntity(EntityManager em) {
        Problem problem = new Problem()
            .title(UPDATED_TITLE)
            .description(UPDATED_DESCRIPTION)
            .level(UPDATED_LEVEL)
            .xpReward(UPDATED_XP_REWARD);
        return problem;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(Problem.class).block();
        } catch (Exception e) {
            // It can fail, if other entities are still referring this - it will be removed later.
        }
    }

    @AfterEach
    public void cleanup() {
        deleteEntities(em);
    }

    @BeforeEach
    public void setupCsrf() {
        webTestClient = webTestClient.mutateWith(csrf());
    }

    @BeforeEach
    public void initTest() {
        deleteEntities(em);
        problem = createEntity(em);
    }

    @Test
    void createProblem() throws Exception {
        int databaseSizeBeforeCreate = problemRepository.findAll().collectList().block().size();
        // Create the Problem
        ProblemDTO problemDTO = problemMapper.toDto(problem);
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(problemDTO))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the Problem in the database
        List<Problem> problemList = problemRepository.findAll().collectList().block();
        assertThat(problemList).hasSize(databaseSizeBeforeCreate + 1);
        Problem testProblem = problemList.get(problemList.size() - 1);
        assertThat(testProblem.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testProblem.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testProblem.getLevel()).isEqualTo(DEFAULT_LEVEL);
        assertThat(testProblem.getXpReward()).isEqualTo(DEFAULT_XP_REWARD);
    }

    @Test
    void createProblemWithExistingId() throws Exception {
        // Create the Problem with an existing ID
        problem.setId(1L);
        ProblemDTO problemDTO = problemMapper.toDto(problem);

        int databaseSizeBeforeCreate = problemRepository.findAll().collectList().block().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(problemDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Problem in the database
        List<Problem> problemList = problemRepository.findAll().collectList().block();
        assertThat(problemList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void checkTitleIsRequired() throws Exception {
        int databaseSizeBeforeTest = problemRepository.findAll().collectList().block().size();
        // set the field null
        problem.setTitle(null);

        // Create the Problem, which fails.
        ProblemDTO problemDTO = problemMapper.toDto(problem);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(problemDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Problem> problemList = problemRepository.findAll().collectList().block();
        assertThat(problemList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkDescriptionIsRequired() throws Exception {
        int databaseSizeBeforeTest = problemRepository.findAll().collectList().block().size();
        // set the field null
        problem.setDescription(null);

        // Create the Problem, which fails.
        ProblemDTO problemDTO = problemMapper.toDto(problem);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(problemDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Problem> problemList = problemRepository.findAll().collectList().block();
        assertThat(problemList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkLevelIsRequired() throws Exception {
        int databaseSizeBeforeTest = problemRepository.findAll().collectList().block().size();
        // set the field null
        problem.setLevel(null);

        // Create the Problem, which fails.
        ProblemDTO problemDTO = problemMapper.toDto(problem);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(problemDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Problem> problemList = problemRepository.findAll().collectList().block();
        assertThat(problemList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkXpRewardIsRequired() throws Exception {
        int databaseSizeBeforeTest = problemRepository.findAll().collectList().block().size();
        // set the field null
        problem.setXpReward(null);

        // Create the Problem, which fails.
        ProblemDTO problemDTO = problemMapper.toDto(problem);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(problemDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Problem> problemList = problemRepository.findAll().collectList().block();
        assertThat(problemList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void getAllProblems() {
        // Initialize the database
        problemRepository.save(problem).block();

        // Get all the problemList
        webTestClient
            .get()
            .uri(ENTITY_API_URL + "?sort=id,desc")
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.[*].id")
            .value(hasItem(problem.getId().intValue()))
            .jsonPath("$.[*].title")
            .value(hasItem(DEFAULT_TITLE))
            .jsonPath("$.[*].description")
            .value(hasItem(DEFAULT_DESCRIPTION))
            .jsonPath("$.[*].level")
            .value(hasItem(DEFAULT_LEVEL))
            .jsonPath("$.[*].xpReward")
            .value(hasItem(DEFAULT_XP_REWARD));
    }

    @Test
    void getProblem() {
        // Initialize the database
        problemRepository.save(problem).block();

        // Get the problem
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, problem.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(problem.getId().intValue()))
            .jsonPath("$.title")
            .value(is(DEFAULT_TITLE))
            .jsonPath("$.description")
            .value(is(DEFAULT_DESCRIPTION))
            .jsonPath("$.level")
            .value(is(DEFAULT_LEVEL))
            .jsonPath("$.xpReward")
            .value(is(DEFAULT_XP_REWARD));
    }

    @Test
    void getNonExistingProblem() {
        // Get the problem
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putExistingProblem() throws Exception {
        // Initialize the database
        problemRepository.save(problem).block();

        int databaseSizeBeforeUpdate = problemRepository.findAll().collectList().block().size();

        // Update the problem
        Problem updatedProblem = problemRepository.findById(problem.getId()).block();
        updatedProblem.title(UPDATED_TITLE).description(UPDATED_DESCRIPTION).level(UPDATED_LEVEL).xpReward(UPDATED_XP_REWARD);
        ProblemDTO problemDTO = problemMapper.toDto(updatedProblem);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, problemDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(problemDTO))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Problem in the database
        List<Problem> problemList = problemRepository.findAll().collectList().block();
        assertThat(problemList).hasSize(databaseSizeBeforeUpdate);
        Problem testProblem = problemList.get(problemList.size() - 1);
        assertThat(testProblem.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testProblem.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testProblem.getLevel()).isEqualTo(UPDATED_LEVEL);
        assertThat(testProblem.getXpReward()).isEqualTo(UPDATED_XP_REWARD);
    }

    @Test
    void putNonExistingProblem() throws Exception {
        int databaseSizeBeforeUpdate = problemRepository.findAll().collectList().block().size();
        problem.setId(count.incrementAndGet());

        // Create the Problem
        ProblemDTO problemDTO = problemMapper.toDto(problem);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, problemDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(problemDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Problem in the database
        List<Problem> problemList = problemRepository.findAll().collectList().block();
        assertThat(problemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchProblem() throws Exception {
        int databaseSizeBeforeUpdate = problemRepository.findAll().collectList().block().size();
        problem.setId(count.incrementAndGet());

        // Create the Problem
        ProblemDTO problemDTO = problemMapper.toDto(problem);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(problemDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Problem in the database
        List<Problem> problemList = problemRepository.findAll().collectList().block();
        assertThat(problemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamProblem() throws Exception {
        int databaseSizeBeforeUpdate = problemRepository.findAll().collectList().block().size();
        problem.setId(count.incrementAndGet());

        // Create the Problem
        ProblemDTO problemDTO = problemMapper.toDto(problem);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(problemDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Problem in the database
        List<Problem> problemList = problemRepository.findAll().collectList().block();
        assertThat(problemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateProblemWithPatch() throws Exception {
        // Initialize the database
        problemRepository.save(problem).block();

        int databaseSizeBeforeUpdate = problemRepository.findAll().collectList().block().size();

        // Update the problem using partial update
        Problem partialUpdatedProblem = new Problem();
        partialUpdatedProblem.setId(problem.getId());

        partialUpdatedProblem.xpReward(UPDATED_XP_REWARD);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedProblem.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedProblem))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Problem in the database
        List<Problem> problemList = problemRepository.findAll().collectList().block();
        assertThat(problemList).hasSize(databaseSizeBeforeUpdate);
        Problem testProblem = problemList.get(problemList.size() - 1);
        assertThat(testProblem.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testProblem.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testProblem.getLevel()).isEqualTo(DEFAULT_LEVEL);
        assertThat(testProblem.getXpReward()).isEqualTo(UPDATED_XP_REWARD);
    }

    @Test
    void fullUpdateProblemWithPatch() throws Exception {
        // Initialize the database
        problemRepository.save(problem).block();

        int databaseSizeBeforeUpdate = problemRepository.findAll().collectList().block().size();

        // Update the problem using partial update
        Problem partialUpdatedProblem = new Problem();
        partialUpdatedProblem.setId(problem.getId());

        partialUpdatedProblem.title(UPDATED_TITLE).description(UPDATED_DESCRIPTION).level(UPDATED_LEVEL).xpReward(UPDATED_XP_REWARD);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedProblem.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedProblem))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Problem in the database
        List<Problem> problemList = problemRepository.findAll().collectList().block();
        assertThat(problemList).hasSize(databaseSizeBeforeUpdate);
        Problem testProblem = problemList.get(problemList.size() - 1);
        assertThat(testProblem.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testProblem.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testProblem.getLevel()).isEqualTo(UPDATED_LEVEL);
        assertThat(testProblem.getXpReward()).isEqualTo(UPDATED_XP_REWARD);
    }

    @Test
    void patchNonExistingProblem() throws Exception {
        int databaseSizeBeforeUpdate = problemRepository.findAll().collectList().block().size();
        problem.setId(count.incrementAndGet());

        // Create the Problem
        ProblemDTO problemDTO = problemMapper.toDto(problem);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, problemDTO.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(problemDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Problem in the database
        List<Problem> problemList = problemRepository.findAll().collectList().block();
        assertThat(problemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchProblem() throws Exception {
        int databaseSizeBeforeUpdate = problemRepository.findAll().collectList().block().size();
        problem.setId(count.incrementAndGet());

        // Create the Problem
        ProblemDTO problemDTO = problemMapper.toDto(problem);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(problemDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Problem in the database
        List<Problem> problemList = problemRepository.findAll().collectList().block();
        assertThat(problemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamProblem() throws Exception {
        int databaseSizeBeforeUpdate = problemRepository.findAll().collectList().block().size();
        problem.setId(count.incrementAndGet());

        // Create the Problem
        ProblemDTO problemDTO = problemMapper.toDto(problem);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(problemDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Problem in the database
        List<Problem> problemList = problemRepository.findAll().collectList().block();
        assertThat(problemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteProblem() {
        // Initialize the database
        problemRepository.save(problem).block();

        int databaseSizeBeforeDelete = problemRepository.findAll().collectList().block().size();

        // Delete the problem
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, problem.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<Problem> problemList = problemRepository.findAll().collectList().block();
        assertThat(problemList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
