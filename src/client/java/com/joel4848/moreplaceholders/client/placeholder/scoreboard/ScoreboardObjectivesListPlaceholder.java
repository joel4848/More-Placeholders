package com.joel4848.moreplaceholders.client.placeholder.scoreboard;

import com.joel4848.moreplaceholders.client.data.ClientSyncedData;
import de.keksuccino.fancymenu.customization.placeholder.DeserializedPlaceholderString;
import de.keksuccino.fancymenu.customization.placeholder.Placeholder;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.scoreboard.ScoreboardObjective;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.stream.Collectors;

public class ScoreboardObjectivesListPlaceholder extends Placeholder {

    public ScoreboardObjectivesListPlaceholder() {
        super("scoreboard_objectives_list");
    }

    @Override
    public boolean canRunAsync() { return false; }

    @Override
    public String getReplacementFor(DeserializedPlaceholderString dps) {
        String separator = dps.values.getOrDefault("separator", ", ");

        Collection<ClientSyncedData.ObjectiveInfo> synced = ClientSyncedData.getObjectives();
        if (!synced.isEmpty()) {
            return synced.stream()
                    .map(ClientSyncedData.ObjectiveInfo::name)
                    .collect(Collectors.joining(separator));
        }

        ClientPlayNetworkHandler conn = MinecraftClient.getInstance().getNetworkHandler();
        if (conn == null) return "";
        return conn.getScoreboard().getObjectives().stream()
                .map(ScoreboardObjective::getName)
                .collect(Collectors.joining(separator));
    }

    @Override
    public @Nullable List<String> getValueNames() { return List.of("separator"); }

    @Override
    public @NotNull String getDisplayName() { return "Scoreboard Objectives List"; }

    @Override
    public @Nullable List<String> getDescription() {
        return List.of("Returns a list of all scoreboard objective names.");
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
