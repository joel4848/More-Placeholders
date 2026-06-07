package com.joel4848.moreplaceholders.client.placeholder.scoreboard;

import com.joel4848.moreplaceholders.client.data.ClientSyncedData;
import de.keksuccino.fancymenu.customization.placeholder.DeserializedPlaceholderString;
import de.keksuccino.fancymenu.customization.placeholder.Placeholder;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.List;

public class ScoreboardObjectiveCountPlaceholder extends Placeholder {

    public ScoreboardObjectiveCountPlaceholder() {
        super("scoreboard_objective_count");
    }

    @Override
    public boolean canRunAsync() { return false; }

    @Override
    public String getReplacementFor(DeserializedPlaceholderString dps) {
        Collection<ClientSyncedData.ObjectiveInfo> synced = ClientSyncedData.getObjectives();
        if (!synced.isEmpty()) return String.valueOf(synced.size());

        ClientPlayNetworkHandler conn = MinecraftClient.getInstance().getNetworkHandler();
        if (conn == null) return "0";
        return String.valueOf(conn.getScoreboard().getObjectives().size());
    }

    @Override
    public @Nullable List<String> getValueNames() { return null; }

    @Override
    public @NotNull String getDisplayName() { return "Scoreboard Objective Count"; }

    @Override
    public @Nullable List<String> getDescription() {
        return List.of("Returns the total number of scoreboard objectives.");
    }

    @Override
    public String getCategory() { return "Scoreboard"; }

    @Override
    public @NotNull DeserializedPlaceholderString getDefaultPlaceholderString() {
        return new DeserializedPlaceholderString(this.getIdentifier(), null, "");
    }
}
