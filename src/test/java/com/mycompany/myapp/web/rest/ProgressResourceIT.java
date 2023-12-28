package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static org.springframework.security.test.web.reactive.server.SecurityMockServerConfigurers.csrf;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.Progress;
import com.mycompany.myapp.domain.enumeration.Status;
import com.mycompany.myapp.repository.EntityManager;
import com.mycompany.myapp.repository.ProgressRepository;
import com.mycompany.myapp.service.dto.ProgressDTO;
import com.mycompany.myapp.service.mapper.ProgressMapper;
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
 * Integration tests for the {@link ProgressResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class ProgressResourceIT {

    private static final Status DEFAULT_STATUS = Status.STARTED;
    private static final Status UPDATED_STATUS = Status.IN_PROGRESS;

    private static final Integer DEFAULT_CURRENT_LESSON = 1;
    private static final Integer UPDATED_CURRENT_LESSON = 2;

    private static final Integer DEFAULT_XP = 0;
    private static final Integer UPDATED_XP = 1;

    private static final String ENTITY_API_URL = "/api/progresses";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ProgressRepository progressRepository;

    @Autowired
    private ProgressMapper progressMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private Progress progress;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Progress createEntity(EntityManager em) {
        Progress progress = new Progress().status(DEFAULT_STATUS).currentLesson(DEFAULT_CURRENT_LESSON).xp(DEFAULT_XP);
        return progress;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Progress createUpdatedEntity(EntityManager em) {
        Progress progress = new Progress().status(UPDATED_STATUS).currentLesson(UPDATED_CURRENT_LESSON).xp(UPDATED_XP);
        return progress;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(Progress.class).block();
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
        progress = createEntity(em);
    }

    @Test
    void createProgress() throws Exception {
        int databaseSizeBeforeCreate = progressRepository.findAll().collectList().block().size();
        // Create the Progress
        ProgressDTO progressDTO = progressMapper.toDto(progress);
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(progressDTO))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the Progress in the database
        List<Progress> progressList = progressRepository.findAll().collectList().block();
        assertThat(progressList).hasSize(databaseSizeBeforeCreate + 1);
        Progress testProgress = progressList.get(progressList.size() - 1);
        assertThat(testProgress.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testProgress.getCurrentLesson()).isEqualTo(DEFAULT_CURRENT_LESSON);
        assertThat(testProgress.getXp()).isEqualTo(DEFAULT_XP);
    }

    @Test
    void createProgressWithExistingId() throws Exception {
        // Create the Progress with an existing ID
        progress.setId(1L);
        ProgressDTO progressDTO = progressMapper.toDto(progress);

        int databaseSizeBeforeCreate = progressRepository.findAll().collectList().block().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(progressDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Progress in the database
        List<Progress> progressList = progressRepository.findAll().collectList().block();
        assertThat(progressList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void checkStatusIsRequired() throws Exception {
        int databaseSizeBeforeTest = progressRepository.findAll().collectList().block().size();
        // set the field null
        progress.setStatus(null);

        // Create the Progress, which fails.
        ProgressDTO progressDTO = progressMapper.toDto(progress);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(progressDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Progress> progressList = progressRepository.findAll().collectList().block();
        assertThat(progressList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkXpIsRequired() throws Exception {
        int databaseSizeBeforeTest = progressRepository.findAll().collectList().block().size();
        // set the field null
        progress.setXp(null);

        // Create the Progress, which fails.
        ProgressDTO progressDTO = progressMapper.toDto(progress);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(progressDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Progress> progressList = progressRepository.findAll().collectList().block();
        assertThat(progressList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void getAllProgressesAsStream() {
        // Initialize the database
        progressRepository.save(progress).block();

        List<Progress> progressList = webTestClient
            .get()
            .uri(ENTITY_API_URL)
            .accept(MediaType.APPLICATION_NDJSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentTypeCompatibleWith(MediaType.APPLICATION_NDJSON)
            .returnResult(ProgressDTO.class)
            .getResponseBody()
            .map(progressMapper::toEntity)
            .filter(progress::equals)
            .collectList()
            .block(Duration.ofSeconds(5));

        assertThat(progressList).isNotNull();
        assertThat(progressList).hasSize(1);
        Progress testProgress = progressList.get(0);
        assertThat(testProgress.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testProgress.getCurrentLesson()).isEqualTo(DEFAULT_CURRENT_LESSON);
        assertThat(testProgress.getXp()).isEqualTo(DEFAULT_XP);
    }

    @Test
    void getAllProgresses() {
        // Initialize the database
        progressRepository.save(progress).block();

        // Get all the progressList
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
            .value(hasItem(progress.getId().intValue()))
            .jsonPath("$.[*].status")
            .value(hasItem(DEFAULT_STATUS.toString()))
            .jsonPath("$.[*].currentLesson")
            .value(hasItem(DEFAULT_CURRENT_LESSON))
            .jsonPath("$.[*].xp")
            .value(hasItem(DEFAULT_XP));
    }

    @Test
    void getProgress() {
        // Initialize the database
        progressRepository.save(progress).block();

        // Get the progress
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, progress.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(progress.getId().intValue()))
            .jsonPath("$.status")
            .value(is(DEFAULT_STATUS.toString()))
            .jsonPath("$.currentLesson")
            .value(is(DEFAULT_CURRENT_LESSON))
            .jsonPath("$.xp")
            .value(is(DEFAULT_XP));
    }

    @Test
    void getNonExistingProgress() {
        // Get the progress
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putExistingProgress() throws Exception {
        // Initialize the database
        progressRepository.save(progress).block();

        int databaseSizeBeforeUpdate = progressRepository.findAll().collectList().block().size();

        // Update the progress
        Progress updatedProgress = progressRepository.findById(progress.getId()).block();
        updatedProgress.status(UPDATED_STATUS).currentLesson(UPDATED_CURRENT_LESSON).xp(UPDATED_XP);
        ProgressDTO progressDTO = progressMapper.toDto(updatedProgress);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, progressDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(progressDTO))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Progress in the database
        List<Progress> progressList = progressRepository.findAll().collectList().block();
        assertThat(progressList).hasSize(databaseSizeBeforeUpdate);
        Progress testProgress = progressList.get(progressList.size() - 1);
        assertThat(testProgress.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testProgress.getCurrentLesson()).isEqualTo(UPDATED_CURRENT_LESSON);
        assertThat(testProgress.getXp()).isEqualTo(UPDATED_XP);
    }

    @Test
    void putNonExistingProgress() throws Exception {
        int databaseSizeBeforeUpdate = progressRepository.findAll().collectList().block().size();
        progress.setId(count.incrementAndGet());

        // Create the Progress
        ProgressDTO progressDTO = progressMapper.toDto(progress);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, progressDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(progressDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Progress in the database
        List<Progress> progressList = progressRepository.findAll().collectList().block();
        assertThat(progressList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchProgress() throws Exception {
        int databaseSizeBeforeUpdate = progressRepository.findAll().collectList().block().size();
        progress.setId(count.incrementAndGet());

        // Create the Progress
        ProgressDTO progressDTO = progressMapper.toDto(progress);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(progressDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Progress in the database
        List<Progress> progressList = progressRepository.findAll().collectList().block();
        assertThat(progressList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamProgress() throws Exception {
        int databaseSizeBeforeUpdate = progressRepository.findAll().collectList().block().size();
        progress.setId(count.incrementAndGet());

        // Create the Progress
        ProgressDTO progressDTO = progressMapper.toDto(progress);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(progressDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Progress in the database
        List<Progress> progressList = progressRepository.findAll().collectList().block();
        assertThat(progressList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateProgressWithPatch() throws Exception {
        // Initialize the database
        progressRepository.save(progress).block();

        int databaseSizeBeforeUpdate = progressRepository.findAll().collectList().block().size();

        // Update the progress using partial update
        Progress partialUpdatedProgress = new Progress();
        partialUpdatedProgress.setId(progress.getId());

        partialUpdatedProgress.currentLesson(UPDATED_CURRENT_LESSON);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedProgress.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedProgress))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Progress in the database
        List<Progress> progressList = progressRepository.findAll().collectList().block();
        assertThat(progressList).hasSize(databaseSizeBeforeUpdate);
        Progress testProgress = progressList.get(progressList.size() - 1);
        assertThat(testProgress.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testProgress.getCurrentLesson()).isEqualTo(UPDATED_CURRENT_LESSON);
        assertThat(testProgress.getXp()).isEqualTo(DEFAULT_XP);
    }

    @Test
    void fullUpdateProgressWithPatch() throws Exception {
        // Initialize the database
        progressRepository.save(progress).block();

        int databaseSizeBeforeUpdate = progressRepository.findAll().collectList().block().size();

        // Update the progress using partial update
        Progress partialUpdatedProgress = new Progress();
        partialUpdatedProgress.setId(progress.getId());

        partialUpdatedProgress.status(UPDATED_STATUS).currentLesson(UPDATED_CURRENT_LESSON).xp(UPDATED_XP);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedProgress.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedProgress))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Progress in the database
        List<Progress> progressList = progressRepository.findAll().collectList().block();
        assertThat(progressList).hasSize(databaseSizeBeforeUpdate);
        Progress testProgress = progressList.get(progressList.size() - 1);
        assertThat(testProgress.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testProgress.getCurrentLesson()).isEqualTo(UPDATED_CURRENT_LESSON);
        assertThat(testProgress.getXp()).isEqualTo(UPDATED_XP);
    }

    @Test
    void patchNonExistingProgress() throws Exception {
        int databaseSizeBeforeUpdate = progressRepository.findAll().collectList().block().size();
        progress.setId(count.incrementAndGet());

        // Create the Progress
        ProgressDTO progressDTO = progressMapper.toDto(progress);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, progressDTO.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(progressDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Progress in the database
        List<Progress> progressList = progressRepository.findAll().collectList().block();
        assertThat(progressList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchProgress() throws Exception {
        int databaseSizeBeforeUpdate = progressRepository.findAll().collectList().block().size();
        progress.setId(count.incrementAndGet());

        // Create the Progress
        ProgressDTO progressDTO = progressMapper.toDto(progress);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(progressDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Progress in the database
        List<Progress> progressList = progressRepository.findAll().collectList().block();
        assertThat(progressList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamProgress() throws Exception {
        int databaseSizeBeforeUpdate = progressRepository.findAll().collectList().block().size();
        progress.setId(count.incrementAndGet());

        // Create the Progress
        ProgressDTO progressDTO = progressMapper.toDto(progress);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(progressDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Progress in the database
        List<Progress> progressList = progressRepository.findAll().collectList().block();
        assertThat(progressList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteProgress() {
        // Initialize the database
        progressRepository.save(progress).block();

        int databaseSizeBeforeDelete = progressRepository.findAll().collectList().block().size();

        // Delete the progress
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, progress.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<Progress> progressList = progressRepository.findAll().collectList().block();
        assertThat(progressList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
