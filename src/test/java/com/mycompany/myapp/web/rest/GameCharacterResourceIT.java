package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static org.springframework.security.test.web.reactive.server.SecurityMockServerConfigurers.csrf;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.GameCharacter;
import com.mycompany.myapp.repository.EntityManager;
import com.mycompany.myapp.repository.GameCharacterRepository;
import com.mycompany.myapp.service.dto.GameCharacterDTO;
import com.mycompany.myapp.service.mapper.GameCharacterMapper;
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
 * Integration tests for the {@link GameCharacterResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class GameCharacterResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBB";

    private static final Integer DEFAULT_LEVEL = 0;
    private static final Integer UPDATED_LEVEL = 1;

    private static final Integer DEFAULT_EXPERIENCE = 0;
    private static final Integer UPDATED_EXPERIENCE = 1;

    private static final String DEFAULT_SHAPE = "AAAAAAAAAA";
    private static final String UPDATED_SHAPE = "BBBBBBBBBB";

    private static final String DEFAULT_COLOR = "AAAAAAAAAA";
    private static final String UPDATED_COLOR = "BBBBBBBBBB";

    private static final String DEFAULT_ACCESSORY = "AAAAAAAAAA";
    private static final String UPDATED_ACCESSORY = "BBBBBBBBBB";

    private static final String DEFAULT_PROGRAMMING_LANGUAGE = "AAAAAAAAAA";
    private static final String UPDATED_PROGRAMMING_LANGUAGE = "BBBBBBBBBB";

    private static final String DEFAULT_UNIQUE_LINK = "AAAAAAAAAA";
    private static final String UPDATED_UNIQUE_LINK = "BBBBBBBBBB";

    private static final String DEFAULT_PROFILE_PICTURE = "AAAAAAAAAA";
    private static final String UPDATED_PROFILE_PICTURE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/game-characters";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private GameCharacterRepository gameCharacterRepository;

    @Autowired
    private GameCharacterMapper gameCharacterMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private GameCharacter gameCharacter;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static GameCharacter createEntity(EntityManager em) {
        GameCharacter gameCharacter = new GameCharacter()
            .name(DEFAULT_NAME)
            .email(DEFAULT_EMAIL)
            .level(DEFAULT_LEVEL)
            .experience(DEFAULT_EXPERIENCE)
            .shape(DEFAULT_SHAPE)
            .color(DEFAULT_COLOR)
            .accessory(DEFAULT_ACCESSORY)
            .programmingLanguage(DEFAULT_PROGRAMMING_LANGUAGE)
            .uniqueLink(DEFAULT_UNIQUE_LINK)
            .profilePicture(DEFAULT_PROFILE_PICTURE);
        return gameCharacter;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static GameCharacter createUpdatedEntity(EntityManager em) {
        GameCharacter gameCharacter = new GameCharacter()
            .name(UPDATED_NAME)
            .email(UPDATED_EMAIL)
            .level(UPDATED_LEVEL)
            .experience(UPDATED_EXPERIENCE)
            .shape(UPDATED_SHAPE)
            .color(UPDATED_COLOR)
            .accessory(UPDATED_ACCESSORY)
            .programmingLanguage(UPDATED_PROGRAMMING_LANGUAGE)
            .uniqueLink(UPDATED_UNIQUE_LINK)
            .profilePicture(UPDATED_PROFILE_PICTURE);
        return gameCharacter;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(GameCharacter.class).block();
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
        gameCharacter = createEntity(em);
    }

    @Test
    void createGameCharacter() throws Exception {
        int databaseSizeBeforeCreate = gameCharacterRepository.findAll().collectList().block().size();
        // Create the GameCharacter
        GameCharacterDTO gameCharacterDTO = gameCharacterMapper.toDto(gameCharacter);
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(gameCharacterDTO))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the GameCharacter in the database
        List<GameCharacter> gameCharacterList = gameCharacterRepository.findAll().collectList().block();
        assertThat(gameCharacterList).hasSize(databaseSizeBeforeCreate + 1);
        GameCharacter testGameCharacter = gameCharacterList.get(gameCharacterList.size() - 1);
        assertThat(testGameCharacter.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testGameCharacter.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testGameCharacter.getLevel()).isEqualTo(DEFAULT_LEVEL);
        assertThat(testGameCharacter.getExperience()).isEqualTo(DEFAULT_EXPERIENCE);
        assertThat(testGameCharacter.getShape()).isEqualTo(DEFAULT_SHAPE);
        assertThat(testGameCharacter.getColor()).isEqualTo(DEFAULT_COLOR);
        assertThat(testGameCharacter.getAccessory()).isEqualTo(DEFAULT_ACCESSORY);
        assertThat(testGameCharacter.getProgrammingLanguage()).isEqualTo(DEFAULT_PROGRAMMING_LANGUAGE);
        assertThat(testGameCharacter.getUniqueLink()).isEqualTo(DEFAULT_UNIQUE_LINK);
        assertThat(testGameCharacter.getProfilePicture()).isEqualTo(DEFAULT_PROFILE_PICTURE);
    }

    @Test
    void createGameCharacterWithExistingId() throws Exception {
        // Create the GameCharacter with an existing ID
        gameCharacter.setId(1L);
        GameCharacterDTO gameCharacterDTO = gameCharacterMapper.toDto(gameCharacter);

        int databaseSizeBeforeCreate = gameCharacterRepository.findAll().collectList().block().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(gameCharacterDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the GameCharacter in the database
        List<GameCharacter> gameCharacterList = gameCharacterRepository.findAll().collectList().block();
        assertThat(gameCharacterList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = gameCharacterRepository.findAll().collectList().block().size();
        // set the field null
        gameCharacter.setName(null);

        // Create the GameCharacter, which fails.
        GameCharacterDTO gameCharacterDTO = gameCharacterMapper.toDto(gameCharacter);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(gameCharacterDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<GameCharacter> gameCharacterList = gameCharacterRepository.findAll().collectList().block();
        assertThat(gameCharacterList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkEmailIsRequired() throws Exception {
        int databaseSizeBeforeTest = gameCharacterRepository.findAll().collectList().block().size();
        // set the field null
        gameCharacter.setEmail(null);

        // Create the GameCharacter, which fails.
        GameCharacterDTO gameCharacterDTO = gameCharacterMapper.toDto(gameCharacter);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(gameCharacterDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<GameCharacter> gameCharacterList = gameCharacterRepository.findAll().collectList().block();
        assertThat(gameCharacterList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkLevelIsRequired() throws Exception {
        int databaseSizeBeforeTest = gameCharacterRepository.findAll().collectList().block().size();
        // set the field null
        gameCharacter.setLevel(null);

        // Create the GameCharacter, which fails.
        GameCharacterDTO gameCharacterDTO = gameCharacterMapper.toDto(gameCharacter);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(gameCharacterDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<GameCharacter> gameCharacterList = gameCharacterRepository.findAll().collectList().block();
        assertThat(gameCharacterList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkExperienceIsRequired() throws Exception {
        int databaseSizeBeforeTest = gameCharacterRepository.findAll().collectList().block().size();
        // set the field null
        gameCharacter.setExperience(null);

        // Create the GameCharacter, which fails.
        GameCharacterDTO gameCharacterDTO = gameCharacterMapper.toDto(gameCharacter);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(gameCharacterDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<GameCharacter> gameCharacterList = gameCharacterRepository.findAll().collectList().block();
        assertThat(gameCharacterList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkShapeIsRequired() throws Exception {
        int databaseSizeBeforeTest = gameCharacterRepository.findAll().collectList().block().size();
        // set the field null
        gameCharacter.setShape(null);

        // Create the GameCharacter, which fails.
        GameCharacterDTO gameCharacterDTO = gameCharacterMapper.toDto(gameCharacter);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(gameCharacterDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<GameCharacter> gameCharacterList = gameCharacterRepository.findAll().collectList().block();
        assertThat(gameCharacterList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkColorIsRequired() throws Exception {
        int databaseSizeBeforeTest = gameCharacterRepository.findAll().collectList().block().size();
        // set the field null
        gameCharacter.setColor(null);

        // Create the GameCharacter, which fails.
        GameCharacterDTO gameCharacterDTO = gameCharacterMapper.toDto(gameCharacter);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(gameCharacterDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<GameCharacter> gameCharacterList = gameCharacterRepository.findAll().collectList().block();
        assertThat(gameCharacterList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkProgrammingLanguageIsRequired() throws Exception {
        int databaseSizeBeforeTest = gameCharacterRepository.findAll().collectList().block().size();
        // set the field null
        gameCharacter.setProgrammingLanguage(null);

        // Create the GameCharacter, which fails.
        GameCharacterDTO gameCharacterDTO = gameCharacterMapper.toDto(gameCharacter);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(gameCharacterDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<GameCharacter> gameCharacterList = gameCharacterRepository.findAll().collectList().block();
        assertThat(gameCharacterList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkUniqueLinkIsRequired() throws Exception {
        int databaseSizeBeforeTest = gameCharacterRepository.findAll().collectList().block().size();
        // set the field null
        gameCharacter.setUniqueLink(null);

        // Create the GameCharacter, which fails.
        GameCharacterDTO gameCharacterDTO = gameCharacterMapper.toDto(gameCharacter);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(gameCharacterDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<GameCharacter> gameCharacterList = gameCharacterRepository.findAll().collectList().block();
        assertThat(gameCharacterList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void getAllGameCharacters() {
        // Initialize the database
        gameCharacterRepository.save(gameCharacter).block();

        // Get all the gameCharacterList
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
            .value(hasItem(gameCharacter.getId().intValue()))
            .jsonPath("$.[*].name")
            .value(hasItem(DEFAULT_NAME))
            .jsonPath("$.[*].email")
            .value(hasItem(DEFAULT_EMAIL))
            .jsonPath("$.[*].level")
            .value(hasItem(DEFAULT_LEVEL))
            .jsonPath("$.[*].experience")
            .value(hasItem(DEFAULT_EXPERIENCE))
            .jsonPath("$.[*].shape")
            .value(hasItem(DEFAULT_SHAPE))
            .jsonPath("$.[*].color")
            .value(hasItem(DEFAULT_COLOR))
            .jsonPath("$.[*].accessory")
            .value(hasItem(DEFAULT_ACCESSORY))
            .jsonPath("$.[*].programmingLanguage")
            .value(hasItem(DEFAULT_PROGRAMMING_LANGUAGE))
            .jsonPath("$.[*].uniqueLink")
            .value(hasItem(DEFAULT_UNIQUE_LINK))
            .jsonPath("$.[*].profilePicture")
            .value(hasItem(DEFAULT_PROFILE_PICTURE));
    }

    @Test
    void getGameCharacter() {
        // Initialize the database
        gameCharacterRepository.save(gameCharacter).block();

        // Get the gameCharacter
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, gameCharacter.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(gameCharacter.getId().intValue()))
            .jsonPath("$.name")
            .value(is(DEFAULT_NAME))
            .jsonPath("$.email")
            .value(is(DEFAULT_EMAIL))
            .jsonPath("$.level")
            .value(is(DEFAULT_LEVEL))
            .jsonPath("$.experience")
            .value(is(DEFAULT_EXPERIENCE))
            .jsonPath("$.shape")
            .value(is(DEFAULT_SHAPE))
            .jsonPath("$.color")
            .value(is(DEFAULT_COLOR))
            .jsonPath("$.accessory")
            .value(is(DEFAULT_ACCESSORY))
            .jsonPath("$.programmingLanguage")
            .value(is(DEFAULT_PROGRAMMING_LANGUAGE))
            .jsonPath("$.uniqueLink")
            .value(is(DEFAULT_UNIQUE_LINK))
            .jsonPath("$.profilePicture")
            .value(is(DEFAULT_PROFILE_PICTURE));
    }

    @Test
    void getNonExistingGameCharacter() {
        // Get the gameCharacter
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putExistingGameCharacter() throws Exception {
        // Initialize the database
        gameCharacterRepository.save(gameCharacter).block();

        int databaseSizeBeforeUpdate = gameCharacterRepository.findAll().collectList().block().size();

        // Update the gameCharacter
        GameCharacter updatedGameCharacter = gameCharacterRepository.findById(gameCharacter.getId()).block();
        updatedGameCharacter
            .name(UPDATED_NAME)
            .email(UPDATED_EMAIL)
            .level(UPDATED_LEVEL)
            .experience(UPDATED_EXPERIENCE)
            .shape(UPDATED_SHAPE)
            .color(UPDATED_COLOR)
            .accessory(UPDATED_ACCESSORY)
            .programmingLanguage(UPDATED_PROGRAMMING_LANGUAGE)
            .uniqueLink(UPDATED_UNIQUE_LINK)
            .profilePicture(UPDATED_PROFILE_PICTURE);
        GameCharacterDTO gameCharacterDTO = gameCharacterMapper.toDto(updatedGameCharacter);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, gameCharacterDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(gameCharacterDTO))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the GameCharacter in the database
        List<GameCharacter> gameCharacterList = gameCharacterRepository.findAll().collectList().block();
        assertThat(gameCharacterList).hasSize(databaseSizeBeforeUpdate);
        GameCharacter testGameCharacter = gameCharacterList.get(gameCharacterList.size() - 1);
        assertThat(testGameCharacter.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testGameCharacter.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testGameCharacter.getLevel()).isEqualTo(UPDATED_LEVEL);
        assertThat(testGameCharacter.getExperience()).isEqualTo(UPDATED_EXPERIENCE);
        assertThat(testGameCharacter.getShape()).isEqualTo(UPDATED_SHAPE);
        assertThat(testGameCharacter.getColor()).isEqualTo(UPDATED_COLOR);
        assertThat(testGameCharacter.getAccessory()).isEqualTo(UPDATED_ACCESSORY);
        assertThat(testGameCharacter.getProgrammingLanguage()).isEqualTo(UPDATED_PROGRAMMING_LANGUAGE);
        assertThat(testGameCharacter.getUniqueLink()).isEqualTo(UPDATED_UNIQUE_LINK);
        assertThat(testGameCharacter.getProfilePicture()).isEqualTo(UPDATED_PROFILE_PICTURE);
    }

    @Test
    void putNonExistingGameCharacter() throws Exception {
        int databaseSizeBeforeUpdate = gameCharacterRepository.findAll().collectList().block().size();
        gameCharacter.setId(count.incrementAndGet());

        // Create the GameCharacter
        GameCharacterDTO gameCharacterDTO = gameCharacterMapper.toDto(gameCharacter);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, gameCharacterDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(gameCharacterDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the GameCharacter in the database
        List<GameCharacter> gameCharacterList = gameCharacterRepository.findAll().collectList().block();
        assertThat(gameCharacterList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchGameCharacter() throws Exception {
        int databaseSizeBeforeUpdate = gameCharacterRepository.findAll().collectList().block().size();
        gameCharacter.setId(count.incrementAndGet());

        // Create the GameCharacter
        GameCharacterDTO gameCharacterDTO = gameCharacterMapper.toDto(gameCharacter);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(gameCharacterDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the GameCharacter in the database
        List<GameCharacter> gameCharacterList = gameCharacterRepository.findAll().collectList().block();
        assertThat(gameCharacterList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamGameCharacter() throws Exception {
        int databaseSizeBeforeUpdate = gameCharacterRepository.findAll().collectList().block().size();
        gameCharacter.setId(count.incrementAndGet());

        // Create the GameCharacter
        GameCharacterDTO gameCharacterDTO = gameCharacterMapper.toDto(gameCharacter);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(gameCharacterDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the GameCharacter in the database
        List<GameCharacter> gameCharacterList = gameCharacterRepository.findAll().collectList().block();
        assertThat(gameCharacterList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateGameCharacterWithPatch() throws Exception {
        // Initialize the database
        gameCharacterRepository.save(gameCharacter).block();

        int databaseSizeBeforeUpdate = gameCharacterRepository.findAll().collectList().block().size();

        // Update the gameCharacter using partial update
        GameCharacter partialUpdatedGameCharacter = new GameCharacter();
        partialUpdatedGameCharacter.setId(gameCharacter.getId());

        partialUpdatedGameCharacter
            .name(UPDATED_NAME)
            .email(UPDATED_EMAIL)
            .experience(UPDATED_EXPERIENCE)
            .color(UPDATED_COLOR)
            .accessory(UPDATED_ACCESSORY)
            .programmingLanguage(UPDATED_PROGRAMMING_LANGUAGE)
            .uniqueLink(UPDATED_UNIQUE_LINK);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedGameCharacter.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedGameCharacter))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the GameCharacter in the database
        List<GameCharacter> gameCharacterList = gameCharacterRepository.findAll().collectList().block();
        assertThat(gameCharacterList).hasSize(databaseSizeBeforeUpdate);
        GameCharacter testGameCharacter = gameCharacterList.get(gameCharacterList.size() - 1);
        assertThat(testGameCharacter.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testGameCharacter.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testGameCharacter.getLevel()).isEqualTo(DEFAULT_LEVEL);
        assertThat(testGameCharacter.getExperience()).isEqualTo(UPDATED_EXPERIENCE);
        assertThat(testGameCharacter.getShape()).isEqualTo(DEFAULT_SHAPE);
        assertThat(testGameCharacter.getColor()).isEqualTo(UPDATED_COLOR);
        assertThat(testGameCharacter.getAccessory()).isEqualTo(UPDATED_ACCESSORY);
        assertThat(testGameCharacter.getProgrammingLanguage()).isEqualTo(UPDATED_PROGRAMMING_LANGUAGE);
        assertThat(testGameCharacter.getUniqueLink()).isEqualTo(UPDATED_UNIQUE_LINK);
        assertThat(testGameCharacter.getProfilePicture()).isEqualTo(DEFAULT_PROFILE_PICTURE);
    }

    @Test
    void fullUpdateGameCharacterWithPatch() throws Exception {
        // Initialize the database
        gameCharacterRepository.save(gameCharacter).block();

        int databaseSizeBeforeUpdate = gameCharacterRepository.findAll().collectList().block().size();

        // Update the gameCharacter using partial update
        GameCharacter partialUpdatedGameCharacter = new GameCharacter();
        partialUpdatedGameCharacter.setId(gameCharacter.getId());

        partialUpdatedGameCharacter
            .name(UPDATED_NAME)
            .email(UPDATED_EMAIL)
            .level(UPDATED_LEVEL)
            .experience(UPDATED_EXPERIENCE)
            .shape(UPDATED_SHAPE)
            .color(UPDATED_COLOR)
            .accessory(UPDATED_ACCESSORY)
            .programmingLanguage(UPDATED_PROGRAMMING_LANGUAGE)
            .uniqueLink(UPDATED_UNIQUE_LINK)
            .profilePicture(UPDATED_PROFILE_PICTURE);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedGameCharacter.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedGameCharacter))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the GameCharacter in the database
        List<GameCharacter> gameCharacterList = gameCharacterRepository.findAll().collectList().block();
        assertThat(gameCharacterList).hasSize(databaseSizeBeforeUpdate);
        GameCharacter testGameCharacter = gameCharacterList.get(gameCharacterList.size() - 1);
        assertThat(testGameCharacter.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testGameCharacter.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testGameCharacter.getLevel()).isEqualTo(UPDATED_LEVEL);
        assertThat(testGameCharacter.getExperience()).isEqualTo(UPDATED_EXPERIENCE);
        assertThat(testGameCharacter.getShape()).isEqualTo(UPDATED_SHAPE);
        assertThat(testGameCharacter.getColor()).isEqualTo(UPDATED_COLOR);
        assertThat(testGameCharacter.getAccessory()).isEqualTo(UPDATED_ACCESSORY);
        assertThat(testGameCharacter.getProgrammingLanguage()).isEqualTo(UPDATED_PROGRAMMING_LANGUAGE);
        assertThat(testGameCharacter.getUniqueLink()).isEqualTo(UPDATED_UNIQUE_LINK);
        assertThat(testGameCharacter.getProfilePicture()).isEqualTo(UPDATED_PROFILE_PICTURE);
    }

    @Test
    void patchNonExistingGameCharacter() throws Exception {
        int databaseSizeBeforeUpdate = gameCharacterRepository.findAll().collectList().block().size();
        gameCharacter.setId(count.incrementAndGet());

        // Create the GameCharacter
        GameCharacterDTO gameCharacterDTO = gameCharacterMapper.toDto(gameCharacter);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, gameCharacterDTO.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(gameCharacterDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the GameCharacter in the database
        List<GameCharacter> gameCharacterList = gameCharacterRepository.findAll().collectList().block();
        assertThat(gameCharacterList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchGameCharacter() throws Exception {
        int databaseSizeBeforeUpdate = gameCharacterRepository.findAll().collectList().block().size();
        gameCharacter.setId(count.incrementAndGet());

        // Create the GameCharacter
        GameCharacterDTO gameCharacterDTO = gameCharacterMapper.toDto(gameCharacter);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(gameCharacterDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the GameCharacter in the database
        List<GameCharacter> gameCharacterList = gameCharacterRepository.findAll().collectList().block();
        assertThat(gameCharacterList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamGameCharacter() throws Exception {
        int databaseSizeBeforeUpdate = gameCharacterRepository.findAll().collectList().block().size();
        gameCharacter.setId(count.incrementAndGet());

        // Create the GameCharacter
        GameCharacterDTO gameCharacterDTO = gameCharacterMapper.toDto(gameCharacter);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(gameCharacterDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the GameCharacter in the database
        List<GameCharacter> gameCharacterList = gameCharacterRepository.findAll().collectList().block();
        assertThat(gameCharacterList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteGameCharacter() {
        // Initialize the database
        gameCharacterRepository.save(gameCharacter).block();

        int databaseSizeBeforeDelete = gameCharacterRepository.findAll().collectList().block().size();

        // Delete the gameCharacter
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, gameCharacter.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<GameCharacter> gameCharacterList = gameCharacterRepository.findAll().collectList().block();
        assertThat(gameCharacterList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
