package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static org.springframework.security.test.web.reactive.server.SecurityMockServerConfigurers.csrf;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.ExecutionCode;
import com.mycompany.myapp.repository.EntityManager;
import com.mycompany.myapp.repository.ExecutionCodeRepository;
import com.mycompany.myapp.service.dto.ExecutionCodeDTO;
import com.mycompany.myapp.service.mapper.ExecutionCodeMapper;
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
import org.springframework.util.Base64Utils;

/**
 * Integration tests for the {@link ExecutionCodeResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class ExecutionCodeResourceIT {

    private static final Integer DEFAULT_QUESTION_NUMBER = 1;
    private static final Integer UPDATED_QUESTION_NUMBER = 2;

    private static final String DEFAULT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_CODE = "BBBBBBBBBB";

    private static final Integer DEFAULT_GAME_CHARACTER = 1;
    private static final Integer UPDATED_GAME_CHARACTER = 2;

    private static final String ENTITY_API_URL = "/api/execution-codes";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ExecutionCodeRepository executionCodeRepository;

    @Autowired
    private ExecutionCodeMapper executionCodeMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private ExecutionCode executionCode;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ExecutionCode createEntity(EntityManager em) {
        ExecutionCode executionCode = new ExecutionCode()
            .questionNumber(DEFAULT_QUESTION_NUMBER)
            .code(DEFAULT_CODE)
            .gameCharacter(DEFAULT_GAME_CHARACTER);
        return executionCode;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ExecutionCode createUpdatedEntity(EntityManager em) {
        ExecutionCode executionCode = new ExecutionCode()
            .questionNumber(UPDATED_QUESTION_NUMBER)
            .code(UPDATED_CODE)
            .gameCharacter(UPDATED_GAME_CHARACTER);
        return executionCode;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(ExecutionCode.class).block();
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
        executionCode = createEntity(em);
    }

    @Test
    void createExecutionCode() throws Exception {
        int databaseSizeBeforeCreate = executionCodeRepository.findAll().collectList().block().size();
        // Create the ExecutionCode
        ExecutionCodeDTO executionCodeDTO = executionCodeMapper.toDto(executionCode);
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(executionCodeDTO))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the ExecutionCode in the database
        List<ExecutionCode> executionCodeList = executionCodeRepository.findAll().collectList().block();
        assertThat(executionCodeList).hasSize(databaseSizeBeforeCreate + 1);
        ExecutionCode testExecutionCode = executionCodeList.get(executionCodeList.size() - 1);
        assertThat(testExecutionCode.getQuestionNumber()).isEqualTo(DEFAULT_QUESTION_NUMBER);
        assertThat(testExecutionCode.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testExecutionCode.getGameCharacter()).isEqualTo(DEFAULT_GAME_CHARACTER);
    }

    @Test
    void createExecutionCodeWithExistingId() throws Exception {
        // Create the ExecutionCode with an existing ID
        executionCode.setId(1L);
        ExecutionCodeDTO executionCodeDTO = executionCodeMapper.toDto(executionCode);

        int databaseSizeBeforeCreate = executionCodeRepository.findAll().collectList().block().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(executionCodeDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the ExecutionCode in the database
        List<ExecutionCode> executionCodeList = executionCodeRepository.findAll().collectList().block();
        assertThat(executionCodeList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void checkQuestionNumberIsRequired() throws Exception {
        int databaseSizeBeforeTest = executionCodeRepository.findAll().collectList().block().size();
        // set the field null
        executionCode.setQuestionNumber(null);

        // Create the ExecutionCode, which fails.
        ExecutionCodeDTO executionCodeDTO = executionCodeMapper.toDto(executionCode);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(executionCodeDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<ExecutionCode> executionCodeList = executionCodeRepository.findAll().collectList().block();
        assertThat(executionCodeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkGameCharacterIsRequired() throws Exception {
        int databaseSizeBeforeTest = executionCodeRepository.findAll().collectList().block().size();
        // set the field null
        executionCode.setGameCharacter(null);

        // Create the ExecutionCode, which fails.
        ExecutionCodeDTO executionCodeDTO = executionCodeMapper.toDto(executionCode);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(executionCodeDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<ExecutionCode> executionCodeList = executionCodeRepository.findAll().collectList().block();
        assertThat(executionCodeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void getAllExecutionCodes() {
        // Initialize the database
        executionCodeRepository.save(executionCode).block();

        // Get all the executionCodeList
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
            .value(hasItem(executionCode.getId().intValue()))
            .jsonPath("$.[*].questionNumber")
            .value(hasItem(DEFAULT_QUESTION_NUMBER))
            .jsonPath("$.[*].code")
            .value(hasItem(DEFAULT_CODE.toString()))
            .jsonPath("$.[*].gameCharacter")
            .value(hasItem(DEFAULT_GAME_CHARACTER));
    }

    @Test
    void getExecutionCode() {
        // Initialize the database
        executionCodeRepository.save(executionCode).block();

        // Get the executionCode
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, executionCode.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(executionCode.getId().intValue()))
            .jsonPath("$.questionNumber")
            .value(is(DEFAULT_QUESTION_NUMBER))
            .jsonPath("$.code")
            .value(is(DEFAULT_CODE.toString()))
            .jsonPath("$.gameCharacter")
            .value(is(DEFAULT_GAME_CHARACTER));
    }

    @Test
    void getNonExistingExecutionCode() {
        // Get the executionCode
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putExistingExecutionCode() throws Exception {
        // Initialize the database
        executionCodeRepository.save(executionCode).block();

        int databaseSizeBeforeUpdate = executionCodeRepository.findAll().collectList().block().size();

        // Update the executionCode
        ExecutionCode updatedExecutionCode = executionCodeRepository.findById(executionCode.getId()).block();
        updatedExecutionCode.questionNumber(UPDATED_QUESTION_NUMBER).code(UPDATED_CODE).gameCharacter(UPDATED_GAME_CHARACTER);
        ExecutionCodeDTO executionCodeDTO = executionCodeMapper.toDto(updatedExecutionCode);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, executionCodeDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(executionCodeDTO))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the ExecutionCode in the database
        List<ExecutionCode> executionCodeList = executionCodeRepository.findAll().collectList().block();
        assertThat(executionCodeList).hasSize(databaseSizeBeforeUpdate);
        ExecutionCode testExecutionCode = executionCodeList.get(executionCodeList.size() - 1);
        assertThat(testExecutionCode.getQuestionNumber()).isEqualTo(UPDATED_QUESTION_NUMBER);
        assertThat(testExecutionCode.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testExecutionCode.getGameCharacter()).isEqualTo(UPDATED_GAME_CHARACTER);
    }

    @Test
    void putNonExistingExecutionCode() throws Exception {
        int databaseSizeBeforeUpdate = executionCodeRepository.findAll().collectList().block().size();
        executionCode.setId(count.incrementAndGet());

        // Create the ExecutionCode
        ExecutionCodeDTO executionCodeDTO = executionCodeMapper.toDto(executionCode);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, executionCodeDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(executionCodeDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the ExecutionCode in the database
        List<ExecutionCode> executionCodeList = executionCodeRepository.findAll().collectList().block();
        assertThat(executionCodeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchExecutionCode() throws Exception {
        int databaseSizeBeforeUpdate = executionCodeRepository.findAll().collectList().block().size();
        executionCode.setId(count.incrementAndGet());

        // Create the ExecutionCode
        ExecutionCodeDTO executionCodeDTO = executionCodeMapper.toDto(executionCode);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(executionCodeDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the ExecutionCode in the database
        List<ExecutionCode> executionCodeList = executionCodeRepository.findAll().collectList().block();
        assertThat(executionCodeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamExecutionCode() throws Exception {
        int databaseSizeBeforeUpdate = executionCodeRepository.findAll().collectList().block().size();
        executionCode.setId(count.incrementAndGet());

        // Create the ExecutionCode
        ExecutionCodeDTO executionCodeDTO = executionCodeMapper.toDto(executionCode);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(executionCodeDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the ExecutionCode in the database
        List<ExecutionCode> executionCodeList = executionCodeRepository.findAll().collectList().block();
        assertThat(executionCodeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateExecutionCodeWithPatch() throws Exception {
        // Initialize the database
        executionCodeRepository.save(executionCode).block();

        int databaseSizeBeforeUpdate = executionCodeRepository.findAll().collectList().block().size();

        // Update the executionCode using partial update
        ExecutionCode partialUpdatedExecutionCode = new ExecutionCode();
        partialUpdatedExecutionCode.setId(executionCode.getId());

        partialUpdatedExecutionCode.gameCharacter(UPDATED_GAME_CHARACTER);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedExecutionCode.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedExecutionCode))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the ExecutionCode in the database
        List<ExecutionCode> executionCodeList = executionCodeRepository.findAll().collectList().block();
        assertThat(executionCodeList).hasSize(databaseSizeBeforeUpdate);
        ExecutionCode testExecutionCode = executionCodeList.get(executionCodeList.size() - 1);
        assertThat(testExecutionCode.getQuestionNumber()).isEqualTo(DEFAULT_QUESTION_NUMBER);
        assertThat(testExecutionCode.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testExecutionCode.getGameCharacter()).isEqualTo(UPDATED_GAME_CHARACTER);
    }

    @Test
    void fullUpdateExecutionCodeWithPatch() throws Exception {
        // Initialize the database
        executionCodeRepository.save(executionCode).block();

        int databaseSizeBeforeUpdate = executionCodeRepository.findAll().collectList().block().size();

        // Update the executionCode using partial update
        ExecutionCode partialUpdatedExecutionCode = new ExecutionCode();
        partialUpdatedExecutionCode.setId(executionCode.getId());

        partialUpdatedExecutionCode.questionNumber(UPDATED_QUESTION_NUMBER).code(UPDATED_CODE).gameCharacter(UPDATED_GAME_CHARACTER);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedExecutionCode.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedExecutionCode))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the ExecutionCode in the database
        List<ExecutionCode> executionCodeList = executionCodeRepository.findAll().collectList().block();
        assertThat(executionCodeList).hasSize(databaseSizeBeforeUpdate);
        ExecutionCode testExecutionCode = executionCodeList.get(executionCodeList.size() - 1);
        assertThat(testExecutionCode.getQuestionNumber()).isEqualTo(UPDATED_QUESTION_NUMBER);
        assertThat(testExecutionCode.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testExecutionCode.getGameCharacter()).isEqualTo(UPDATED_GAME_CHARACTER);
    }

    @Test
    void patchNonExistingExecutionCode() throws Exception {
        int databaseSizeBeforeUpdate = executionCodeRepository.findAll().collectList().block().size();
        executionCode.setId(count.incrementAndGet());

        // Create the ExecutionCode
        ExecutionCodeDTO executionCodeDTO = executionCodeMapper.toDto(executionCode);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, executionCodeDTO.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(executionCodeDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the ExecutionCode in the database
        List<ExecutionCode> executionCodeList = executionCodeRepository.findAll().collectList().block();
        assertThat(executionCodeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchExecutionCode() throws Exception {
        int databaseSizeBeforeUpdate = executionCodeRepository.findAll().collectList().block().size();
        executionCode.setId(count.incrementAndGet());

        // Create the ExecutionCode
        ExecutionCodeDTO executionCodeDTO = executionCodeMapper.toDto(executionCode);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(executionCodeDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the ExecutionCode in the database
        List<ExecutionCode> executionCodeList = executionCodeRepository.findAll().collectList().block();
        assertThat(executionCodeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamExecutionCode() throws Exception {
        int databaseSizeBeforeUpdate = executionCodeRepository.findAll().collectList().block().size();
        executionCode.setId(count.incrementAndGet());

        // Create the ExecutionCode
        ExecutionCodeDTO executionCodeDTO = executionCodeMapper.toDto(executionCode);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(executionCodeDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the ExecutionCode in the database
        List<ExecutionCode> executionCodeList = executionCodeRepository.findAll().collectList().block();
        assertThat(executionCodeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteExecutionCode() {
        // Initialize the database
        executionCodeRepository.save(executionCode).block();

        int databaseSizeBeforeDelete = executionCodeRepository.findAll().collectList().block().size();

        // Delete the executionCode
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, executionCode.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<ExecutionCode> executionCodeList = executionCodeRepository.findAll().collectList().block();
        assertThat(executionCodeList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
