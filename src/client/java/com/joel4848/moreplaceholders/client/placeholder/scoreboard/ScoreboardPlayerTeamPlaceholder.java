package com.joel4848.moreplaceholders.client.placeholder.scoreboard;

import de.keksuccino.fancymenu.customization.placeholder.DeserializedPlaceholderString;
import de.keksuccino.fancymenu.customization.placeholder.Placeholder;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.scoreboard.Team;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

public class ScoreboardPlayerTeamPlaceholder extends Placeholder {

    public ScoreboardPlayerTeamPlaceholder() {
        super("player_team");
    }

    @Override
    public boolean canRunAsync() { return false; }

    @Override
    public String getReplacementFor(DeserializedPlaceholderString dps) {
        String playerName = dps.values.get("player_name");
        if (playerName == null) return "";

        ClientPlayNetworkHandler conn = MinecraftClient.getInstance().getNetworkHandler();
        if (conn == null) return "";
        Team team = conn.getScoreboard().getScoreHolderTeam(playerName);
        return team != null ? team.getName() : "";
    }

    @Override
    public @Nullable List<String> getValueNames() { return List.of("player_name"); }

    @Override
    public @NotNull String getDisplayName() { return "Player Team"; }

    @Override
    public @Nullable List<String> getDescription() {
        return List.of(
                "Returns the scoreboard team name of the specified player.",
                "Requires Extra Placeholders on the server for full functionality."
        );
    }

    @Override
    public String getCategory() { return "Scoreboard"; }

    @Override
    public @NotNull DeserializedPlaceholderString getDefaultPlaceholderString() {
        HashMap<String, String> values = new LinkedHashMap<>();
        values.put("player_name", "my_team");
        return new DeserializedPlaceholderString(this.getIdentifier(), values, "");
    }
}
