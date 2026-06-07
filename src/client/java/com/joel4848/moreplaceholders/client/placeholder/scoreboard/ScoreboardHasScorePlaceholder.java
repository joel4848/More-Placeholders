package com.joel4848.moreplaceholders.client.placeholder.scoreboard;

import com.joel4848.moreplaceholders.client.data.ClientSyncedData;
import de.keksuccino.fancymenu.customization.placeholder.DeserializedPlaceholderString;
import de.keksuccino.fancymenu.customization.placeholder.Placeholder;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.scoreboard.ScoreHolder;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.scoreboard.ScoreboardObjective;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

public class ScoreboardHasScorePlaceholder extends Placeholder {

    public ScoreboardHasScorePlaceholder() {
        super("scoreboard_has_score");
    }

    @Override
    public boolean canRunAsync() { return false; }

    @Override
    public String getReplacementFor(DeserializedPlaceholderString dps) {
        String playerName = dps.values.get("player");
        String objectiveName = dps.values.get("objective");
        if (playerName == null || objectiveName == null) return "false";

        // Synced data check
        if (!ClientSyncedData.getObjectives().isEmpty()) {
            return String.valueOf(ClientSyncedData.getScore(playerName, objectiveName) != null);
        }

        // Vanilla fallback
        ClientPlayNetworkHandler conn = MinecraftClient.getInstance().getNetworkHandler();
        if (conn == null) return "false";
        Scoreboard sb = conn.getScoreboard();
        ScoreboardObjective obj = sb.getNullableObjective(objectiveName);
        if (obj == null) return "false";
        return String.valueOf(sb.getScore(ScoreHolder.fromName(playerName), obj) != null);
    }

    @Override
    public @Nullable List<String> getValueNames() { return List.of("player", "objective"); }

    @Override
    public @NotNull String getDisplayName() { return "Scoreboard Has Score"; }

    @Override
    public @Nullable List<String> getDescription() {
        return List.of(
                "Returns 'true' if the player has a score for the given objective.",
                "Requires Extra Placeholders on the server for full functionality."
        );
    }

    @Override
    public String getCategory() { return "Scoreboard"; }

    @Override
    public @NotNull DeserializedPlaceholderString getDefaultPlaceholderString() {
        HashMap<String, String> values = new LinkedHashMap<>();
        values.put("player", "Player1");
        values.put("objective", "my_objective");
        return new DeserializedPlaceholderString(this.getIdentifier(), values, "");
    }
}
