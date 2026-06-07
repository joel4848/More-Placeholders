package com.joel4848.moreplaceholders.client.placeholder.scoreboard;

import com.joel4848.moreplaceholders.client.data.ClientSyncedData;
import de.keksuccino.fancymenu.customization.placeholder.DeserializedPlaceholderString;
import de.keksuccino.fancymenu.customization.placeholder.Placeholder;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.scoreboard.ScoreboardObjective;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

public class ScoreboardObjectiveDisplayNamePlaceholder extends Placeholder {

    public ScoreboardObjectiveDisplayNamePlaceholder() {
        super("scoreboard_objective_display_name");
    }

    @Override
    public boolean canRunAsync() { return false; }

    @Override
    public String getReplacementFor(DeserializedPlaceholderString dps) {
        String objectiveName = dps.values.get("objective");
        if (objectiveName == null) return "";

        // Synced data
        ClientSyncedData.ObjectiveInfo info = ClientSyncedData.getObjective(objectiveName);
        if (info != null) return info.displayName();

        // Vanilla fallback
        ClientPlayNetworkHandler conn = MinecraftClient.getInstance().getNetworkHandler();
        if (conn == null) return "";
        ScoreboardObjective obj = conn.getScoreboard().getNullableObjective(objectiveName);
        return obj != null ? obj.getDisplayName().getString() : "";
    }

    @Override
    public @Nullable List<String> getValueNames() { return List.of("objective"); }

    @Override
    public @NotNull String getDisplayName() { return "Scoreboard Objective Display Name"; }

    @Override
    public @Nullable List<String> getDescription() {
        return List.of("Returns the display name of a scoreboard objective.");
    }

    @Override
    public String getCategory() { return "Scoreboard"; }

    @Override
    public @NotNull DeserializedPlaceholderString getDefaultPlaceholderString() {
        HashMap<String, String> values = new LinkedHashMap<>();
        values.put("objective", "my_objective");
        return new DeserializedPlaceholderString(this.getIdentifier(), values, "");
    }
}
