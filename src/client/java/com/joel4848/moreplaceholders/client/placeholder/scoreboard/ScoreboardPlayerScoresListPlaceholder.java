package com.joel4848.moreplaceholders.client.placeholder.scoreboard;

import com.joel4848.moreplaceholders.client.data.ClientSyncedData;
import de.keksuccino.fancymenu.customization.placeholder.DeserializedPlaceholderString;
import de.keksuccino.fancymenu.customization.placeholder.Placeholder;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ScoreboardPlayerScoresListPlaceholder extends Placeholder {

    public ScoreboardPlayerScoresListPlaceholder() {
        super("scoreboard_player_scores_list");
    }

    @Override
    public boolean canRunAsync() { return false; }

    @Override
    public String getReplacementFor(DeserializedPlaceholderString dps) {
        String playerName = dps.values.get("player");
        if (playerName == null) return "";
        String separator = dps.values.getOrDefault("separator", ", ");
        String format = dps.values.getOrDefault("format", "%objective%: %score%");

        Map<String, Integer> objectiveScores = ClientSyncedData.getObjectivesForHolder(playerName);
        if (objectiveScores.isEmpty()) return "";

        return objectiveScores.entrySet().stream()
                .map(e -> format
                        .replace("%objective%", e.getKey())
                        .replace("%score%", String.valueOf(e.getValue())))
                .collect(Collectors.joining(separator));
    }

    @Override
    public @Nullable List<String> getValueNames() { return List.of("player", "separator", "format"); }

    @Override
    public @NotNull String getDisplayName() { return "Scoreboard Player Scores List"; }

    @Override
    public @Nullable List<String> getDescription() {
        return List.of(
                "Returns a list of all scores for a player across all objectives.",
                "Requires Extra Placeholders on the server for full functionality."
        );
    }

    @Override
    public String getCategory() { return "Scoreboard"; }

    @Override
    public @NotNull DeserializedPlaceholderString getDefaultPlaceholderString() {
        HashMap<String, String> values = new LinkedHashMap<>();
        values.put("player", "Player1");
        values.put("separator", ", ");
        values.put("format", "%objective%: %my_objective%");
        return new DeserializedPlaceholderString(this.getIdentifier(), values, "");
    }
}
