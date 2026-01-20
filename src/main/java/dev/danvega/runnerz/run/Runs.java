package dev.danvega.runnerz.run;

import java.util.List;

/**
 * A wrapper record for a collection of runs.
 * Used for JSON deserialization of run data.
 *
 * @param runs the list of runs
 */
public record Runs (List<Run> runs) {

}
