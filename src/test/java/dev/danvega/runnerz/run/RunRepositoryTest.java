package dev.danvega.runnerz.run;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class RunRepositoryTest {

    @Autowired
    private RunRepository runRepository;

    private Run sampleRun;

    @BeforeEach
    void setUp() {
        runRepository.deleteAll();
        sampleRun = new Run(
                null,
                "Morning Run",
                LocalDateTime.of(2024, 1, 1, 6, 0),
                LocalDateTime.of(2024, 1, 1, 7, 0),
                5,
                Location.OUTDOOR
        );
    }

    @Test
    void findAll_ShouldReturnAllRuns() {
        Run savedRun = runRepository.save(sampleRun);

        List<Run> result = runRepository.findAll();

        assertEquals(1, result.size());
        assertEquals(savedRun.getId(), result.get(0).getId());
    }

    @Test
    void findById_WhenRunExists_ShouldReturnRun() {
        Run savedRun = runRepository.save(sampleRun);

        Optional<Run> result = runRepository.findById(savedRun.getId());

        assertTrue(result.isPresent());
        assertEquals(savedRun.getId(), result.get().getId());
        assertEquals("Morning Run", result.get().getTitle());
    }

    @Test
    void findById_WhenRunDoesNotExist_ShouldReturnEmpty() {
        Optional<Run> result = runRepository.findById(999);

        assertTrue(result.isEmpty());
    }

    @Test
    void save_ShouldInsertRun() {
        Run savedRun = runRepository.save(sampleRun);

        assertNotNull(savedRun.getId());
        assertEquals("Morning Run", savedRun.getTitle());
        assertEquals(5, savedRun.getMiles());
        assertEquals(Location.OUTDOOR, savedRun.getLocation());
    }

    @Test
    void save_ShouldUpdateExistingRun() {
        Run savedRun = runRepository.save(sampleRun);
        savedRun.setTitle("Updated Run");
        savedRun.setMiles(10);

        Run updatedRun = runRepository.save(savedRun);

        assertEquals(savedRun.getId(), updatedRun.getId());
        assertEquals("Updated Run", updatedRun.getTitle());
        assertEquals(10, updatedRun.getMiles());
    }

    @Test
    void deleteById_ShouldDeleteRun() {
        Run savedRun = runRepository.save(sampleRun);
        Integer id = savedRun.getId();

        runRepository.deleteById(id);

        Optional<Run> result = runRepository.findById(id);
        assertTrue(result.isEmpty());
    }

    @Test
    void count_ShouldReturnNumberOfRuns() {
        runRepository.save(sampleRun);
        Run run2 = new Run(
                null,
                "Evening Run",
                LocalDateTime.of(2024, 1, 1, 18, 0),
                LocalDateTime.of(2024, 1, 1, 19, 0),
                3,
                Location.INDOOR
        );
        runRepository.save(run2);

        long count = runRepository.count();

        assertEquals(2, count);
    }

    @Test
    void saveAll_ShouldSaveAllRuns() {
        Run run2 = new Run(
                null,
                "Evening Run",
                LocalDateTime.of(2024, 1, 1, 18, 0),
                LocalDateTime.of(2024, 1, 1, 19, 0),
                3,
                Location.INDOOR
        );
        List<Run> runs = List.of(sampleRun, run2);

        List<Run> savedRuns = runRepository.saveAll(runs);

        assertEquals(2, savedRuns.size());
        assertNotNull(savedRuns.get(0).getId());
        assertNotNull(savedRuns.get(1).getId());
    }

    @Test
    void findByLocation_ShouldReturnRunsWithMatchingLocation() {
        runRepository.save(sampleRun);
        Run indoorRun = new Run(
                null,
                "Indoor Run",
                LocalDateTime.of(2024, 1, 1, 18, 0),
                LocalDateTime.of(2024, 1, 1, 19, 0),
                3,
                Location.INDOOR
        );
        runRepository.save(indoorRun);

        List<Run> outdoorRuns = runRepository.findByLocation(Location.OUTDOOR);

        assertEquals(1, outdoorRuns.size());
        assertEquals("Morning Run", outdoorRuns.get(0).getTitle());
    }

    @Test
    void findByLocation_WhenNoMatches_ShouldReturnEmptyList() {
        runRepository.save(sampleRun);

        List<Run> indoorRuns = runRepository.findByLocation(Location.INDOOR);

        assertTrue(indoorRuns.isEmpty());
    }

    @Test
    void findAllPaginated_ShouldReturnPagedResults() {
        for (int i = 0; i < 15; i++) {
            Run run = new Run(
                    null,
                    "Run " + i,
                    LocalDateTime.of(2024, 1, 1, 6, 0).plusDays(i),
                    LocalDateTime.of(2024, 1, 1, 7, 0).plusDays(i),
                    5,
                    Location.OUTDOOR
            );
            runRepository.save(run);
        }

        Page<Run> firstPage = runRepository.findAll(PageRequest.of(0, 10));

        assertEquals(10, firstPage.getContent().size());
        assertEquals(15, firstPage.getTotalElements());
        assertEquals(2, firstPage.getTotalPages());
    }

    @Test
    void findAllPaginated_SecondPage_ShouldReturnRemainingResults() {
        for (int i = 0; i < 15; i++) {
            Run run = new Run(
                    null,
                    "Run " + i,
                    LocalDateTime.of(2024, 1, 1, 6, 0).plusDays(i),
                    LocalDateTime.of(2024, 1, 1, 7, 0).plusDays(i),
                    5,
                    Location.OUTDOOR
            );
            runRepository.save(run);
        }

        Page<Run> secondPage = runRepository.findAll(PageRequest.of(1, 10));

        assertEquals(5, secondPage.getContent().size());
    }
}
