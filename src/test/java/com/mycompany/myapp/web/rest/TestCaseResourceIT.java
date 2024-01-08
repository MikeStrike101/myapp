package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static org.springframework.security.test.web.reactive.server.SecurityMockServerConfigurers.csrf;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.TestCase;
import com.mycompany.myapp.repository.EntityManager;
import com.mycompany.myapp.repository.TestCaseRepository;
import com.mycompany.myapp.service.dto.TestCaseDTO;
import com.mycompany.myapp.service.mapper.TestCaseMapper;
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
 * Integration tests for the {@link TestCaseResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class TestCaseResourceIT {

    private static final String DEFAULT_INPUT = "AAAAAAAAAA";
    private static final String UPDATED_INPUT = "BBBBBBBBBB";

    private static final String DEFAULT_OUTPUT = "AAAAAAAAAA";
    private static final String UPDATED_OUTPUT = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/test-cases";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private TestCaseRepository testCaseRepository;

    @Autowired
    private TestCaseMapper testCaseMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private TestCase testCase;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TestCase createEntity(EntityManager em) {
        TestCase testCase = new TestCase().input(DEFAULT_INPUT).output(DEFAULT_OUTPUT);
        return testCase;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TestCase createUpdatedEntity(EntityManager em) {
        TestCase testCase = new TestCase().input(UPDATED_INPUT).output(UPDATED_OUTPUT);
        return testCase;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(TestCase.class).block();
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
        testCase = createEntity(em);
    }

    @Test
    void createTestCase() throws Exception {
        int databaseSizeBeforeCreate = testCaseRepository.findAll().collectList().block().size();
        // Create the TestCase
        TestCaseDTO testCaseDTO = testCaseMapper.toDto(testCase);
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(testCaseDTO))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the TestCase in the database
        List<TestCase> testCaseList = testCaseRepository.findAll().collectList().block();
        assertThat(testCaseList).hasSize(databaseSizeBeforeCreate + 1);
        TestCase testTestCase = testCaseList.get(testCaseList.size() - 1);
        assertThat(testTestCase.getInput()).isEqualTo(DEFAULT_INPUT);
        assertThat(testTestCase.getOutput()).isEqualTo(DEFAULT_OUTPUT);
    }

    @Test
    void createTestCaseWithExistingId() throws Exception {
        // Create the TestCase with an existing ID
        testCase.setId(1L);
        TestCaseDTO testCaseDTO = testCaseMapper.toDto(testCase);

        int databaseSizeBeforeCreate = testCaseRepository.findAll().collectList().block().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(testCaseDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the TestCase in the database
        List<TestCase> testCaseList = testCaseRepository.findAll().collectList().block();
        assertThat(testCaseList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void checkInputIsRequired() throws Exception {
        int databaseSizeBeforeTest = testCaseRepository.findAll().collectList().block().size();
        // set the field null
        testCase.setInput(null);

        // Create the TestCase, which fails.
        TestCaseDTO testCaseDTO = testCaseMapper.toDto(testCase);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(testCaseDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<TestCase> testCaseList = testCaseRepository.findAll().collectList().block();
        assertThat(testCaseList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkOutputIsRequired() throws Exception {
        int databaseSizeBeforeTest = testCaseRepository.findAll().collectList().block().size();
        // set the field null
        testCase.setOutput(null);

        // Create the TestCase, which fails.
        TestCaseDTO testCaseDTO = testCaseMapper.toDto(testCase);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(testCaseDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<TestCase> testCaseList = testCaseRepository.findAll().collectList().block();
        assertThat(testCaseList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void getAllTestCases() {
        // Initialize the database
        testCaseRepository.save(testCase).block();

        // Get all the testCaseList
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
            .value(hasItem(testCase.getId().intValue()))
            .jsonPath("$.[*].input")
            .value(hasItem(DEFAULT_INPUT))
            .jsonPath("$.[*].output")
            .value(hasItem(DEFAULT_OUTPUT));
    }

    @Test
    void getTestCase() {
        // Initialize the database
        testCaseRepository.save(testCase).block();

        // Get the testCase
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, testCase.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(testCase.getId().intValue()))
            .jsonPath("$.input")
            .value(is(DEFAULT_INPUT))
            .jsonPath("$.output")
            .value(is(DEFAULT_OUTPUT));
    }

    @Test
    void getNonExistingTestCase() {
        // Get the testCase
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putExistingTestCase() throws Exception {
        // Initialize the database
        testCaseRepository.save(testCase).block();

        int databaseSizeBeforeUpdate = testCaseRepository.findAll().collectList().block().size();

        // Update the testCase
        TestCase updatedTestCase = testCaseRepository.findById(testCase.getId()).block();
        updatedTestCase.input(UPDATED_INPUT).output(UPDATED_OUTPUT);
        TestCaseDTO testCaseDTO = testCaseMapper.toDto(updatedTestCase);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, testCaseDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(testCaseDTO))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the TestCase in the database
        List<TestCase> testCaseList = testCaseRepository.findAll().collectList().block();
        assertThat(testCaseList).hasSize(databaseSizeBeforeUpdate);
        TestCase testTestCase = testCaseList.get(testCaseList.size() - 1);
        assertThat(testTestCase.getInput()).isEqualTo(UPDATED_INPUT);
        assertThat(testTestCase.getOutput()).isEqualTo(UPDATED_OUTPUT);
    }

    @Test
    void putNonExistingTestCase() throws Exception {
        int databaseSizeBeforeUpdate = testCaseRepository.findAll().collectList().block().size();
        testCase.setId(count.incrementAndGet());

        // Create the TestCase
        TestCaseDTO testCaseDTO = testCaseMapper.toDto(testCase);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, testCaseDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(testCaseDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the TestCase in the database
        List<TestCase> testCaseList = testCaseRepository.findAll().collectList().block();
        assertThat(testCaseList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchTestCase() throws Exception {
        int databaseSizeBeforeUpdate = testCaseRepository.findAll().collectList().block().size();
        testCase.setId(count.incrementAndGet());

        // Create the TestCase
        TestCaseDTO testCaseDTO = testCaseMapper.toDto(testCase);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(testCaseDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the TestCase in the database
        List<TestCase> testCaseList = testCaseRepository.findAll().collectList().block();
        assertThat(testCaseList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamTestCase() throws Exception {
        int databaseSizeBeforeUpdate = testCaseRepository.findAll().collectList().block().size();
        testCase.setId(count.incrementAndGet());

        // Create the TestCase
        TestCaseDTO testCaseDTO = testCaseMapper.toDto(testCase);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(testCaseDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the TestCase in the database
        List<TestCase> testCaseList = testCaseRepository.findAll().collectList().block();
        assertThat(testCaseList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateTestCaseWithPatch() throws Exception {
        // Initialize the database
        testCaseRepository.save(testCase).block();

        int databaseSizeBeforeUpdate = testCaseRepository.findAll().collectList().block().size();

        // Update the testCase using partial update
        TestCase partialUpdatedTestCase = new TestCase();
        partialUpdatedTestCase.setId(testCase.getId());

        partialUpdatedTestCase.input(UPDATED_INPUT).output(UPDATED_OUTPUT);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedTestCase.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedTestCase))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the TestCase in the database
        List<TestCase> testCaseList = testCaseRepository.findAll().collectList().block();
        assertThat(testCaseList).hasSize(databaseSizeBeforeUpdate);
        TestCase testTestCase = testCaseList.get(testCaseList.size() - 1);
        assertThat(testTestCase.getInput()).isEqualTo(UPDATED_INPUT);
        assertThat(testTestCase.getOutput()).isEqualTo(UPDATED_OUTPUT);
    }

    @Test
    void fullUpdateTestCaseWithPatch() throws Exception {
        // Initialize the database
        testCaseRepository.save(testCase).block();

        int databaseSizeBeforeUpdate = testCaseRepository.findAll().collectList().block().size();

        // Update the testCase using partial update
        TestCase partialUpdatedTestCase = new TestCase();
        partialUpdatedTestCase.setId(testCase.getId());

        partialUpdatedTestCase.input(UPDATED_INPUT).output(UPDATED_OUTPUT);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedTestCase.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedTestCase))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the TestCase in the database
        List<TestCase> testCaseList = testCaseRepository.findAll().collectList().block();
        assertThat(testCaseList).hasSize(databaseSizeBeforeUpdate);
        TestCase testTestCase = testCaseList.get(testCaseList.size() - 1);
        assertThat(testTestCase.getInput()).isEqualTo(UPDATED_INPUT);
        assertThat(testTestCase.getOutput()).isEqualTo(UPDATED_OUTPUT);
    }

    @Test
    void patchNonExistingTestCase() throws Exception {
        int databaseSizeBeforeUpdate = testCaseRepository.findAll().collectList().block().size();
        testCase.setId(count.incrementAndGet());

        // Create the TestCase
        TestCaseDTO testCaseDTO = testCaseMapper.toDto(testCase);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, testCaseDTO.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(testCaseDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the TestCase in the database
        List<TestCase> testCaseList = testCaseRepository.findAll().collectList().block();
        assertThat(testCaseList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchTestCase() throws Exception {
        int databaseSizeBeforeUpdate = testCaseRepository.findAll().collectList().block().size();
        testCase.setId(count.incrementAndGet());

        // Create the TestCase
        TestCaseDTO testCaseDTO = testCaseMapper.toDto(testCase);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(testCaseDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the TestCase in the database
        List<TestCase> testCaseList = testCaseRepository.findAll().collectList().block();
        assertThat(testCaseList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamTestCase() throws Exception {
        int databaseSizeBeforeUpdate = testCaseRepository.findAll().collectList().block().size();
        testCase.setId(count.incrementAndGet());

        // Create the TestCase
        TestCaseDTO testCaseDTO = testCaseMapper.toDto(testCase);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(testCaseDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the TestCase in the database
        List<TestCase> testCaseList = testCaseRepository.findAll().collectList().block();
        assertThat(testCaseList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteTestCase() {
        // Initialize the database
        testCaseRepository.save(testCase).block();

        int databaseSizeBeforeDelete = testCaseRepository.findAll().collectList().block().size();

        // Delete the testCase
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, testCase.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<TestCase> testCaseList = testCaseRepository.findAll().collectList().block();
        assertThat(testCaseList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
