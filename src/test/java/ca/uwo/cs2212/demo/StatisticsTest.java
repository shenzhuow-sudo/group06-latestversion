package ca.uwo.cs2212.demo;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class StatisticsTest {
    @Test
    void updateFrom_accumulatesSavedPlayTimeAndCorrectWords() {
        Statistics statistics = new Statistics();

        statistics.updateFrom(new SessionResult(100, 50.0, 90.0, 1, 30, 8));
        statistics.updateFrom(new SessionResult(140, 60.0, 95.0, 0, 45, 12));

        assertEquals(75, statistics.getTotalTime());
        assertEquals(20, statistics.getWordsTyped());
    }
}
