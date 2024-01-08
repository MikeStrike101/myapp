package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static org.springframework.security.test.web.reactive.server.SecurityMockServerConfigurers.csrf;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.UserProblem;
import com.mycompany.myapp.repository.EntityManager;
import com.mycompany.myapp.repository.UserProblemRepository;
import com.mycompany.myapp.service.dto.UserProblemDTO;
import com.mycompany.myapp.service.mapper.UserProblemMapper;
import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
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
 * Integration tests for the {@link UserProblemResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class UserProblemResourceIT {

    private static final Instant DEFAULT_SOLVED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_SOLVED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Integer DEFAULT_PASSED_TEST_CASES = 0;
    private static final Integer UPDATED_PASSED_TEST_CASES = 1;

    private static final Integer DEFAULT_XP_AWARDED = 0;
    private static final Integer UPDATED_XP_AWARDED = 1;

    private static final String ENTITY_API_URL = "/api/user-problems";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private UserProblemRepository userProblemRepository;

    @Autowired
    private UserProblemMapper userProblemMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private UserProblem userProblem;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static UserProblem createEntity(EntityManager em) {
        UserProblem userProblem = new UserProblem()
            .solvedAt(DEFAULT_SOLVED_AT)
            .passedTestCases(DEFAULT_PASSED_TEST_CASES)
            .xpAwarded(DEFAULT_XP_AWARDED);
        return userProblem;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static UserProblem createUpdatedEntity(EntityManager em) {
        UserProblem userProblem = new UserProblem()
            .solvedAt(UPDATED_SOLVED_AT)
            .passedTestCases(UPDATED_PASSED_TEST_CASES)
            .xpAwarded(UPDATED_XP_AWARDED);
        return userProblem;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(UserProblem.class).block();
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
        userProblem = createEntity(em);
    }

    @Test
    void createUserProblem() throws Exception {
        int databaseSizeBeforeCreate = userProblemRepository.findAll().collectList().block().size();
        // Create the UserProblem
        UserProblemDTO userProblemDTO = userProblemMapper.toDto(userProblem);
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(userProblemDTO))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the UserProblem in the database
        List<UserProblem> userProblemList = userProblemRepository.findAll().collectList().block();
        assertThat(userProblemList).hasSize(databaseSizeBeforeCreate + 1);
        UserProblem testUserProblem = userProblemList.get(userProblemList.size() - 1);
        assertThat(testUserProblem.getSolvedAt()).isEqualTo(DEFAULT_SOLVED_AT);
        assertThat(testUserProblem.getPassedTestCases()).isEqualTo(DEFAULT_PASSED_TEST_CASES);
        assertThat(testUserProblem.getXpAwarded()).isEqualTo(DEFAULT_XP_AWARDED);
    }

    @Test
    void createUserProblemWithExistingId() throws Exception {
        // Create the UserProblem with an existing ID
        userProblem.setId(1L);
        UserProblemDTO userProblemDTO = userProblemMapper.toDto(userProblem);

        int databaseSizeBeforeCreate = userProblemRepository.findAll().collectList().block().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(userProblemDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the UserProblem in the database
        List<UserProblem> userProblemList = userProblemRepository.findAll().collectList().block();
        assertThat(userProblemList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void checkPassedTestCasesIsRequired() throws Exception {
        int databaseSizeBeforeTest = userProblemRepository.findAll().collectList().block().size();
        // set the field null
        userProblem.setPassedTestCases(null);

        // Create the UserProblem, which fails.
        UserProblemDTO userProblemDTO = userProblemMapper.toDto(userProblem);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(userProblemDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<UserProblem> userProblemList = userProblemRepository.findAll().collectList().block();
        assertThat(userProblemList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkXpAwardedIsRequired() throws Exception {
        int databaseSizeBeforeTest = userProblemRepository.findAll().collectList().block().size();
        // set the field null
        userProblem.setXpAwarded(null);

        // Create the UserProblem, which fails.
        UserProblemDTO userProblemDTO = userProblemMapper.toDto(userProblem);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(userProblemDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<UserProblem> userProblemList = userProblemRepository.findAll().collectList().block();
        assertThat(userProblemList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void getAllUserProblems() {
        // Initialize the database
        userProblemRepository.save(userProblem).block();

        // Get all the userProblemList
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
            .value(hasItem(userProblem.getId().intValue()))
            .jsonPath("$.[*].solvedAt")
            .value(hasItem(DEFAULT_SOLVED_AT.toString()))
            .jsonPath("$.[*].passedTestCases")
            .value(hasItem(DEFAULT_PASSED_TEST_CASES))
            .jsonPath("$.[*].xpAwarded")
            .value(hasItem(DEFAULT_XP_AWARDED));
    }

    @Test
    void getUserProblem() {
        // Initialize the database
        userProblemRepository.save(userProblem).block();

        // Get the userProblem
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, userProblem.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(userProblem.getId().intValue()))
            .jsonPath("$.solvedAt")
            .value(is(DEFAULT_SOLVED_AT.toString()))
            .jsonPath("$.passedTestCases")
            .value(is(DEFAULT_PASSED_TEST_CASES))
            .jsonPath("$.xpAwarded")
            .value(is(DEFAULT_XP_AWARDED));
    }

    @Test
    void getNonExistingUserProblem() {
        // Get the userProblem
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putExistingUserProblem() throws Exception {
        // Initialize the database
        userProblemRepository.save(userProblem).block();

        int databaseSizeBeforeUpdate = userProblemRepository.findAll().collectList().block().size();

        // Update the userProblem
        UserProblem updatedUserProblem = userProblemRepository.findById(userProblem.getId()).block();
        updatedUserProblem.solvedAt(UPDATED_SOLVED_AT).passedTestCases(UPDATED_PASSED_TEST_CASES).xpAwarded(UPDATED_XP_AWARDED);
        UserProblemDTO userProblemDTO = userProblemMapper.toDto(updatedUserProblem);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, userProblemDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(userProblemDTO))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the UserProblem in the database
        List<UserProblem> userProblemList = userProblemRepository.findAll().collectList().block();
        assertThat(userProblemList).hasSize(databaseSizeBeforeUpdate);
        UserProblem testUserProblem = userProblemList.get(userProblemList.size() - 1);
        assertThat(testUserProblem.getSolvedAt()).isEqualTo(UPDATED_SOLVED_AT);
        assertThat(testUserProblem.getPassedTestCases()).isEqualTo(UPDATED_PASSED_TEST_CASES);
        assertThat(testUserProblem.getXpAwarded()).isEqualTo(UPDATED_XP_AWARDED);
    }

    @Test
    void putNonExistingUserProblem() throws Exception {
        int databaseSizeBeforeUpdate = userProblemRepository.findAll().collectList().block().size();
        userProblem.setId(count.incrementAndGet());

        // Create the UserProblem
        UserProblemDTO userProblemDTO = userProblemMapper.toDto(userProblem);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, userProblemDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(userProblemDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the UserProblem in the database
        List<UserProblem> userProblemList = userProblemRepository.findAll().collectList().block();
        assertThat(userProblemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchUserProblem() throws Exception {
        int databaseSizeBeforeUpdate = userProblemRepository.findAll().collectList().block().size();
        userProblem.setId(count.incrementAndGet());

        // Create the UserProblem
        UserProblemDTO userProblemDTO = userProblemMapper.toDto(userProblem);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(userProblemDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the UserProblem in the database
        List<UserProblem> userProblemList = userProblemRepository.findAll().collectList().block();
        assertThat(userProblemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamUserProblem() throws Exception {
        int databaseSizeBeforeUpdate = userProblemRepository.findAll().collectList().block().size();
        userProblem.setId(count.incrementAndGet());

        // Create the UserProblem
        UserProblemDTO userProblemDTO = userProblemMapper.toDto(userProblem);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(userProblemDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the UserProblem in the database
        List<UserProblem> userProblemList = userProblemRepository.findAll().collectList().block();
        assertThat(userProblemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateUserProblemWithPatch() throws Exception {
        // Initialize the database
        userProblemRepository.save(userProblem).block();

        int databaseSizeBeforeUpdate = userProblemRepository.findAll().collectList().block().size();

        // Update the userProblem using partial update
        UserProblem partialUpdatedUserProblem = new UserProblem();
        partialUpdatedUserProblem.setId(userProblem.getId());

        partialUpdatedUserProblem.solvedAt(UPDATED_SOLVED_AT).xpAwarded(UPDATED_XP_AWARDED);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedUserProblem.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedUserProblem))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the UserProblem in the database
        List<UserProblem> userProblemList = userProblemRepository.findAll().collectList().block();
        assertThat(userProblemList).hasSize(databaseSizeBeforeUpdate);
        UserProblem testUserProblem = userProblemList.get(userProblemList.size() - 1);
        assertThat(testUserProblem.getSolvedAt()).isEqualTo(UPDATED_SOLVED_AT);
        assertThat(testUserProblem.getPassedTestCases()).isEqualTo(DEFAULT_PASSED_TEST_CASES);
        assertThat(testUserProblem.getXpAwarded()).isEqualTo(UPDATED_XP_AWARDED);
    }

    @Test
    void fullUpdateUserProblemWithPatch() throws Exception {
        // Initialize the database
        userProblemRepository.save(userProblem).block();

        int databaseSizeBeforeUpdate = userProblemRepository.findAll().collectList().block().size();

        // Update the userProblem using partial update
        UserProblem partialUpdatedUserProblem = new UserProblem();
        partialUpdatedUserProblem.setId(userProblem.getId());

        partialUpdatedUserProblem.solvedAt(UPDATED_SOLVED_AT).passedTestCases(UPDATED_PASSED_TEST_CASES).xpAwarded(UPDATED_XP_AWARDED);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedUserProblem.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedUserProblem))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the UserProblem in the database
        List<UserProblem> userProblemList = userProblemRepository.findAll().collectList().block();
        assertThat(userProblemList).hasSize(databaseSizeBeforeUpdate);
        UserProblem testUserProblem = userProblemList.get(userProblemList.size() - 1);
        assertThat(testUserProblem.getSolvedAt()).isEqualTo(UPDATED_SOLVED_AT);
        assertThat(testUserProblem.getPassedTestCases()).isEqualTo(UPDATED_PASSED_TEST_CASES);
        assertThat(testUserProblem.getXpAwarded()).isEqualTo(UPDATED_XP_AWARDED);
    }

    @Test
    void patchNonExistingUserProblem() throws Exception {
        int databaseSizeBeforeUpdate = userProblemRepository.findAll().collectList().block().size();
        userProblem.setId(count.incrementAndGet());

        // Create the UserProblem
        UserProblemDTO userProblemDTO = userProblemMapper.toDto(userProblem);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, userProblemDTO.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(userProblemDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the UserProblem in the database
        List<UserProblem> userProblemList = userProblemRepository.findAll().collectList().block();
        assertThat(userProblemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchUserProblem() throws Exception {
        int databaseSizeBeforeUpdate = userProblemRepository.findAll().collectList().block().size();
        userProblem.setId(count.incrementAndGet());

        // Create the UserProblem
        UserProblemDTO userProblemDTO = userProblemMapper.toDto(userProblem);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(userProblemDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the UserProblem in the database
        List<UserProblem> userProblemList = userProblemRepository.findAll().collectList().block();
        assertThat(userProblemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamUserProblem() throws Exception {
        int databaseSizeBeforeUpdate = userProblemRepository.findAll().collectList().block().size();
        userProblem.setId(count.incrementAndGet());

        // Create the UserProblem
        UserProblemDTO userProblemDTO = userProblemMapper.toDto(userProblem);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(userProblemDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the UserProblem in the database
        List<UserProblem> userProblemList = userProblemRepository.findAll().collectList().block();
        assertThat(userProblemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteUserProblem() {
        // Initialize the database
        userProblemRepository.save(userProblem).block();

        int databaseSizeBeforeDelete = userProblemRepository.findAll().collectList().block().size();

        // Delete the userProblem
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, userProblem.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<UserProblem> userProblemList = userProblemRepository.findAll().collectList().block();
        assertThat(userProblemList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
