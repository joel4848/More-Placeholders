package com.joel4848.moreplaceholders.client.placeholder.scoreboard;

import com.joel4848.moreplaceholders.client.data.ClientSyncedData;
import de.keksuccino.fancymenu.customization.placeholder.DeserializedPlaceholderString;
import de.keksuccino.fancymenu.customization.placeholder.Placeholder;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.scoreboard.ScoreHolder;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.stream.Collectors;

public class ScoreboardTrackedPlayersPlaceholder extends Placeholder {

    public ScoreboardTrackedPlayersPlaceholder() {
        super("scoreboard_tracked_players");
    }

    @Override
    public boolean canRunAsync() { return false; }

    @Override
    public String getReplacementFor(DeserializedPlaceholderString dps) {
        String separator = dps.values.getOrDefault("separator", ", ");

        Collection<ClientSyncedData.ObjectiveInfo> objectives = ClientSyncedData.getObjectives();
        if (!objectives.isEmpty()) {
            Set<String> holders = new TreeSet<>();
            for (ClientSyncedData.ObjectiveInfo obj : objectives) {
                holders.addAll(ClientSyncedData.getScoresForObjective(obj.name()).keySet());
            }
            if (!holders.isEmpty()) return String.join(separator, holders);
        }

        ClientPlayNetworkHandler conn = MinecraftClient.getInstance().getNetworkHandler();
        if (conn == null) return "";
        return conn.getScoreboard().getKnownScoreHolders().stream()
                .map(ScoreHolder::getNameForScoreboard)
                .collect(Collectors.joining(separator));
    }

    @Override
    public @Nullable List<String> getValueNames() { return List.of("separator"); }

    @Override
    public @NotNull String getDisplayName() { return "Scoreboard Tracked Players"; }

    @Override
    public @Nullable List<String> getDescription() {
        return List.of("Returns a list of all score holders tracked by the scoreboard.");
    }

    @Override
    public String getCategory() { return "Scoreboard"; }

    @Override
    public @NotNull DeserializedPlaceholderString getDefaultPlaceholderString() {
        HashMap<String, String> values = new LinkedHashMap<>();
        values.put("separator", ", ");
        return new DeserializedPlaceholderString(this.getIdentifier(), values, "");
    }
}
