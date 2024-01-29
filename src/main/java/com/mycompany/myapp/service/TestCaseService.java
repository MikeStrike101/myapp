package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.TestCase;
import com.mycompany.myapp.repository.TestCaseRepository;
import com.mycompany.myapp.service.dto.TestCaseDTO;
import com.mycompany.myapp.service.mapper.TestCaseMapper;
import java.util.NoSuchElementException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link TestCase}.
 */
@Service
@Transactional
public class TestCaseService {

    private final Logger log = LoggerFactory.getLogger(TestCaseService.class);

    private final TestCaseRepository testCaseRepository;

    private final TestCaseMapper testCaseMapper;

    public TestCaseService(TestCaseRepository testCaseRepository, TestCaseMapper testCaseMapper) {
        this.testCaseRepository = testCaseRepository;
        this.testCaseMapper = testCaseMapper;
    }

    /**
     * Save a testCase.
     *
     * @param testCaseDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<TestCaseDTO> save(TestCaseDTO testCaseDTO) {
        log.debug("Request to save TestCase : {}", testCaseDTO);
        return testCaseRepository.save(testCaseMapper.toEntity(testCaseDTO)).map(testCaseMapper::toDto);
    }

    /**
     * Update a testCase.
     *
     * @param testCaseDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<TestCaseDTO> update(TestCaseDTO testCaseDTO) {
        log.debug("Request to update TestCase : {}", testCaseDTO);
        return testCaseRepository.save(testCaseMapper.toEntity(testCaseDTO)).map(testCaseMapper::toDto);
    }

    /**
     * Partially update a testCase.
     *
     * @param testCaseDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Mono<TestCaseDTO> partialUpdate(TestCaseDTO testCaseDTO) {
        log.debug("Request to partially update TestCase : {}", testCaseDTO);

        return testCaseRepository
            .findById(testCaseDTO.getId())
            .map(existingTestCase -> {
                testCaseMapper.partialUpdate(existingTestCase, testCaseDTO);

                return existingTestCase;
            })
            .flatMap(testCaseRepository::save)
            .map(testCaseMapper::toDto);
    }

    /**
     * Get all the testCases.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Flux<TestCaseDTO> findAll(Pageable pageable) {
        log.debug("Request to get all TestCases");
        return testCaseRepository.findAllBy(pageable).map(testCaseMapper::toDto);
    }

    /**
     * Returns the number of testCases available.
     * @return the number of entities in the database.
     *
     */
    public Mono<Long> countAll() {
        return testCaseRepository.count();
    }

    /**
     * Get one testCase by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Mono<TestCaseDTO> findOne(Long id) {
        log.debug("Request to get TestCase : {}", id);
        return testCaseRepository.findById(id).map(testCaseMapper::toDto);
    }

    /**
     * Delete the testCase by id.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    public Mono<Void> delete(Long id) {
        log.debug("Request to delete TestCase : {}", id);
        return testCaseRepository.deleteById(id);
    }

    public Mono<String> getExpectedOutputByQuestionId(Integer questionId) {
        return testCaseRepository
            .findByProblem(questionId)
            .doOnNext(testCase -> log.debug("Fetched TestCase for questionId {}: {}", questionId, testCase))
            .map(TestCase::getOutput)
            .doOnNext(output -> log.debug("Expected output for questionId {}: {}", questionId, output))
            .switchIfEmpty(Mono.error(new NoSuchElementException("Test case not found for question ID: " + questionId)));
    }
}
