package com.joel4848.moreplaceholders.client.placeholder.scoreboard;

import com.joel4848.moreplaceholders.client.data.ClientSyncedData;
import de.keksuccino.fancymenu.customization.placeholder.DeserializedPlaceholderString;
import de.keksuccino.fancymenu.customization.placeholder.Placeholder;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.scoreboard.ReadableScoreboardScore;
import net.minecraft.scoreboard.ScoreHolder;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.scoreboard.ScoreboardObjective;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

public class ScoreboardScorePlaceholder extends Placeholder {

    public ScoreboardScorePlaceholder() {
        super("scoreboard_score");
    }

    @Override
    public boolean canRunAsync() { return false; }

    @Override
    public String getReplacementFor(DeserializedPlaceholderString dps) {
        String playerName = dps.values.get("player");
        String objectiveName = dps.values.get("objective");
        if (playerName == null || objectiveName == null) return "";

        Integer synced = ClientSyncedData.getScore(playerName, objectiveName);
        if (synced != null) return String.valueOf(synced);

        ClientPlayNetworkHandler conn = MinecraftClient.getInstance().getNetworkHandler();
        if (conn == null) return "";
        Scoreboard sb = conn.getScoreboard();
        ScoreboardObjective obj = sb.getNullableObjective(objectiveName);
        if (obj == null) return "";
        ReadableScoreboardScore score = sb.getScore(ScoreHolder.fromName(playerName), obj);
        return score != null ? String.valueOf(score.getScore()) : "";
    }

    @Override
    public @Nullable List<String> getValueNames() {
        return List.of("player", "objective");
    }

    @Override
    public @NotNull String getDisplayName() {
        return "Scoreboard Score";
    }

    @Override
    public @Nullable List<String> getDescription() {
        return List.of(
                "Returns the score of a player for a scoreboard objective.",
                "Requires Extra Placeholders on the server for full functionality."
        );
    }

    @Override
    public String getCategory() {
        return "Scoreboard";
    }

    @Override
    public @NotNull DeserializedPlaceholderString getDefaultPlaceholderString() {
        HashMap<String, String> values = new LinkedHashMap<>();
        values.put("player", "Player1");
        values.put("objective", "my_objective");
        return new DeserializedPlaceholderString(this.getIdentifier(), values, "");
    }
}
