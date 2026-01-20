package dev.danvega.runnerz.run;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.jdbc.core.simple.JdbcClient;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RunRepositoryTest {

    @Mock
    private JdbcClient jdbcClient;

    @Mock
    private JdbcClient.StatementSpec statementSpec;

    @Mock
    private JdbcClient.MappedQuerySpec<Run> mappedQuerySpec;

    @Mock
    private JdbcClient.ResultQuerySpec resultQuerySpec;

    private RunRepository runRepository;

    private Run sampleRun;

    @BeforeEach
    void setUp() {
        runRepository = new RunRepository(jdbcClient);
        sampleRun = new Run(
                1,
                "Morning Run",
                LocalDateTime.of(2024, 1, 1, 6, 0),
                LocalDateTime.of(2024, 1, 1, 7, 0),
                5,
                Location.OUTDOOR
        );
    }

    @Test
    void findAll_ShouldReturnAllRuns() {
        List<Run> expectedRuns = List.of(sampleRun);

        when(jdbcClient.sql(anyString())).thenReturn(statementSpec);
        when(statementSpec.query(Run.class)).thenReturn(mappedQuerySpec);
        when(mappedQuerySpec.list()).thenReturn(expectedRuns);

        List<Run> result = runRepository.findAll();

        assertEquals(expectedRuns, result);
        verify(jdbcClient).sql("select * from run");
    }

    @Test
    void findById_WhenRunExists_ShouldReturnRun() {
        when(jdbcClient.sql(anyString())).thenReturn(statementSpec);
        when(statementSpec.param("id", 1)).thenReturn(statementSpec);
        when(statementSpec.query(Run.class)).thenReturn(mappedQuerySpec);
        when(mappedQuerySpec.optional()).thenReturn(Optional.of(sampleRun));

        Optional<Run> result = runRepository.findById(1);

        assertTrue(result.isPresent());
        assertEquals(sampleRun, result.get());
    }

    @Test
    void findById_WhenRunDoesNotExist_ShouldReturnEmpty() {
        when(jdbcClient.sql(anyString())).thenReturn(statementSpec);
        when(statementSpec.param("id", 999)).thenReturn(statementSpec);
        when(statementSpec.query(Run.class)).thenReturn(mappedQuerySpec);
        when(mappedQuerySpec.optional()).thenReturn(Optional.empty());

        Optional<Run> result = runRepository.findById(999);

        assertTrue(result.isEmpty());
    }

    @Test
    void create_ShouldInsertRun() {
        when(jdbcClient.sql(anyString())).thenReturn(statementSpec);
        when(statementSpec.params(anyList())).thenReturn(statementSpec);
        when(statementSpec.update()).thenReturn(1);

        assertDoesNotThrow(() -> runRepository.create(sampleRun));

        verify(jdbcClient).sql(contains("INSERT INTO RUN"));
        verify(statementSpec).params(List.of(
                sampleRun.id(),
                sampleRun.title(),
                sampleRun.startedOn(),
                sampleRun.completedOn(),
                sampleRun.miles(),
                sampleRun.location().toString()
        ));
    }

    @Test
    void create_WhenInsertFails_ShouldThrowException() {
        when(jdbcClient.sql(anyString())).thenReturn(statementSpec);
        when(statementSpec.params(anyList())).thenReturn(statementSpec);
        when(statementSpec.update()).thenReturn(0);

        assertThrows(IllegalStateException.class, () -> runRepository.create(sampleRun));
    }

    @Test
    void update_ShouldUpdateRun() {
        when(jdbcClient.sql(anyString())).thenReturn(statementSpec);
        when(statementSpec.params(any(), any(), any(), any(), any(), any())).thenReturn(statementSpec);
        when(statementSpec.update()).thenReturn(1);

        assertDoesNotThrow(() -> runRepository.update(sampleRun, 1));

        verify(jdbcClient).sql(contains("UPDATE RUN SET"));
    }

    @Test
    void update_WhenUpdateFails_ShouldThrowException() {
        when(jdbcClient.sql(anyString())).thenReturn(statementSpec);
        when(statementSpec.params(any(), any(), any(), any(), any(), any())).thenReturn(statementSpec);
        when(statementSpec.update()).thenReturn(0);

        assertThrows(IllegalStateException.class, () -> runRepository.update(sampleRun, 1));
    }

    @Test
    void delete_ShouldDeleteRun() {
        when(jdbcClient.sql(anyString())).thenReturn(statementSpec);
        when(statementSpec.param("id", 1)).thenReturn(statementSpec);
        when(statementSpec.update()).thenReturn(1);

        assertDoesNotThrow(() -> runRepository.delete(1));

        verify(jdbcClient).sql(contains("DELETE"));
    }

    @Test
    void delete_WhenDeleteFails_ShouldThrowException() {
        when(jdbcClient.sql(anyString())).thenReturn(statementSpec);
        when(statementSpec.param("id", 999)).thenReturn(statementSpec);
        when(statementSpec.update()).thenReturn(0);

        assertThrows(IllegalStateException.class, () -> runRepository.delete(999));
    }

    @Test
    void count_ShouldReturnNumberOfRuns() {
        List<Map<String, Object>> rows = List.of(
                Map.of("id", 1),
                Map.of("id", 2),
                Map.of("id", 3)
        );

        when(jdbcClient.sql(anyString())).thenReturn(statementSpec);
        when(statementSpec.query()).thenReturn(resultQuerySpec);
        when(resultQuerySpec.listOfRows()).thenReturn(rows);

        int count = runRepository.count();

        assertEquals(3, count);
    }

    @Test
    void saveAll_ShouldSaveAllRuns() {
        Run run2 = new Run(
                2,
                "Evening Run",
                LocalDateTime.of(2024, 1, 1, 18, 0),
                LocalDateTime.of(2024, 1, 1, 19, 0),
                3,
                Location.INDOOR
        );
        List<Run> runs = List.of(sampleRun, run2);

        when(jdbcClient.sql(anyString())).thenReturn(statementSpec);
        when(statementSpec.params(anyList())).thenReturn(statementSpec);
        when(statementSpec.update()).thenReturn(1);

        assertDoesNotThrow(() -> runRepository.saveAll(runs));

        verify(statementSpec, times(2)).update();
    }

    @Test
    void findByLocation_ShouldReturnRunsWithMatchingLocation() {
        List<Run> expectedRuns = List.of(sampleRun);

        when(jdbcClient.sql(anyString())).thenReturn(statementSpec);
        when(statementSpec.param("location", "OUTDOOR")).thenReturn(statementSpec);
        when(statementSpec.query(Run.class)).thenReturn(mappedQuerySpec);
        when(mappedQuerySpec.list()).thenReturn(expectedRuns);

        List<Run> result = runRepository.findByLocation("OUTDOOR");

        assertEquals(expectedRuns, result);
        verify(statementSpec).param("location", "OUTDOOR");
    }

    @Test
    void findByLocation_WhenNoMatches_ShouldReturnEmptyList() {
        when(jdbcClient.sql(anyString())).thenReturn(statementSpec);
        when(statementSpec.param("location", "INDOOR")).thenReturn(statementSpec);
        when(statementSpec.query(Run.class)).thenReturn(mappedQuerySpec);
        when(mappedQuerySpec.list()).thenReturn(List.of());

        List<Run> result = runRepository.findByLocation("INDOOR");

        assertTrue(result.isEmpty());
    }

    @Test
    void findAllPaginated_ShouldReturnPagedResults() {
        List<Run> expectedRuns = List.of(sampleRun);

        when(jdbcClient.sql(anyString())).thenReturn(statementSpec);
        when(statementSpec.param("limit", 10)).thenReturn(statementSpec);
        when(statementSpec.param("offset", 0)).thenReturn(statementSpec);
        when(statementSpec.query(Run.class)).thenReturn(mappedQuerySpec);
        when(mappedQuerySpec.list()).thenReturn(expectedRuns);

        List<Run> result = runRepository.findAll(0, 10);

        assertEquals(expectedRuns, result);
    }

    @Test
    void findAllPaginated_SecondPage_ShouldCalculateCorrectOffset() {
        when(jdbcClient.sql(anyString())).thenReturn(statementSpec);
        when(statementSpec.param("limit", 10)).thenReturn(statementSpec);
        when(statementSpec.param("offset", 10)).thenReturn(statementSpec);
        when(statementSpec.query(Run.class)).thenReturn(mappedQuerySpec);
        when(mappedQuerySpec.list()).thenReturn(List.of());

        runRepository.findAll(1, 10);

        verify(statementSpec).param("offset", 10);
    }
}
