package com.joel4848.moreplaceholders.client.placeholder.scoreboard;

import com.joel4848.moreplaceholders.client.data.ClientSyncedData;
import de.keksuccino.fancymenu.customization.placeholder.DeserializedPlaceholderString;
import de.keksuccino.fancymenu.customization.placeholder.Placeholder;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.stream.Collectors;

public class PlayersWithScorePlaceholder extends Placeholder {

    public PlayersWithScorePlaceholder() {
        super("players_with_score");
    }

    @Override
    public boolean canRunAsync() { return false; }

    @Override
    public String getReplacementFor(DeserializedPlaceholderString dps) {
        String objectiveName = dps.values.get("objective");
        String scoreSpec = dps.values.get("score");
        if (objectiveName == null || scoreSpec == null) return "";
        String separator = dps.values.getOrDefault("separator", ", ");

        ScoreRange range = parseRange(scoreSpec.trim());
        if (range == null) return "";

        Map<String, Integer> scores = ClientSyncedData.getScoresForObjective(objectiveName);
        if (scores.isEmpty()) return "";

        return scores.entrySet().stream()
                .filter(e -> range.matches(e.getValue()))
                .map(Map.Entry::getKey)
                .sorted()
                .collect(Collectors.joining(separator));
    }

    @Override
    public @Nullable List<String> getValueNames() {
        return List.of("objective", "score", "separator");
    }

    @Override
    public @NotNull String getDisplayName() { return "Players With Score"; }

    @Override
    public @Nullable List<String> getDescription() {
        return List.of(
                "Returns a list of players whose score for an objective matches a value or range.",
                "Score syntax: '5' (exact), '3..7' (range), '3..' (min), '..7' (max).",
                "Requires Extra Placeholders on the server for full functionality."
        );
    }

    @Override
    public String getCategory() { return "Scoreboard"; }

    @Override
    public @NotNull DeserializedPlaceholderString getDefaultPlaceholderString() {
        HashMap<String, String> values = new LinkedHashMap<>();
        values.put("objective", "my_objective");
        values.put("score", "1..");
        values.put("separator", ", ");
        return new DeserializedPlaceholderString(this.getIdentifier(), values, "");
    }

    @Nullable
    private static ScoreRange parseRange(String spec) {
        if (spec.contains("..")) {
            String[] parts = spec.split("\\.\\.", -1);
            if (parts.length != 2) return null;
            try {
                Integer min = parts[0].isEmpty() ? null : Integer.parseInt(parts[0]);
                Integer max = parts[1].isEmpty() ? null : Integer.parseInt(parts[1]);
                return new ScoreRange(min, max);
            } catch (NumberFormatException e) {
                return null;
            }
        } else {
            try {
                int val = Integer.parseInt(spec);
                return new ScoreRange(val, val);
            } catch (NumberFormatException e) {
                return null;
            }
        }
    }

    private record ScoreRange(@Nullable Integer min, @Nullable Integer max) {
        boolean matches(int score) {
            if (min != null && score < min) return false;
            if (max != null && score > max) return false;
            return true;
        }
    }
}
