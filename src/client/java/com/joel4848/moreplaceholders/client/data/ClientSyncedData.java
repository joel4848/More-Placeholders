package com.joel4848.moreplaceholders.client.data;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

//Placeholders use this, or the limited vanilla behaviour if mod is not on the server
public final class ClientSyncedData {

    private ClientSyncedData() {}

    private static final Map<String, ObjectiveInfo> objectives = new ConcurrentHashMap<>();

    private static final Map<String, Map<String, Integer>> scores = new ConcurrentHashMap<>();

    private static final Map<String, Set<String>> tags = new ConcurrentHashMap<>();

    public static void updateScoreboard(
            List<ObjectiveInfo> newObjectives,
            List<ScoreEntry> newScores
    ) {
        objectives.clear();
        for (ObjectiveInfo o : newObjectives) {
            objectives.put(o.name(), o);
        }
        scores.clear();
        for (ScoreEntry s : newScores) {
            scores.computeIfAbsent(s.objectiveName(), k -> new ConcurrentHashMap<>())
                  .put(s.holderName(), s.value());
        }
    }

    public static void updateTags(Map<String, Set<String>> newTags) {
        tags.clear();
        tags.putAll(newTags);
    }

    public static void clear() {
        objectives.clear();
        scores.clear();
        tags.clear();
    }

    public static Collection<ObjectiveInfo> getObjectives() {
        return Collections.unmodifiableCollection(objectives.values());
    }

    public static ObjectiveInfo getObjective(String name) {
        return objectives.get(name);
    }

    public static Integer getScore(String holderName, String objectiveName) {
        Map<String, Integer> objScores = scores.get(objectiveName);
        if (objScores == null) return null;
        return objScores.get(holderName);
    }

    public static Map<String, Integer> getScoresForObjective(String objectiveName) {
        return scores.getOrDefault(objectiveName, Collections.emptyMap());
    }

    public static Map<String, Integer> getObjectivesForHolder(String holderName) {
        Map<String, Integer> result = new HashMap<>();
        for (Map.Entry<String, Map<String, Integer>> entry : scores.entrySet()) {
            Integer val = entry.getValue().get(holderName);
            if (val != null) result.put(entry.getKey(), val);
        }
        return result;
    }

    public static Set<String> getTags(String playerName) {
        return tags.getOrDefault(playerName, Collections.emptySet());
    }

    public static boolean hasTag(String playerName, String tag) {
        return getTags(playerName).contains(tag);
    }

    public record ObjectiveInfo(
            String name,
            String displayName,
            String criteriaName,
            String renderType
    ) {}

    public record ScoreEntry(String holderName, String objectiveName, int value) {}
}
