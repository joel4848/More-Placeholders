package com.joel4848.moreplaceholders.client.placeholder.world;

import com.joel4848.moreplaceholders.client.data.ClientSyncedData;
import de.keksuccino.fancymenu.customization.placeholder.DeserializedPlaceholderString;
import de.keksuccino.fancymenu.customization.placeholder.Placeholder;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class PlayerTagsListPlaceholder extends Placeholder {

    public PlayerTagsListPlaceholder() {
        super("player_tags_list");
    }

    @Override
    public boolean canRunAsync() { return false; }

    @Override
    public String getReplacementFor(DeserializedPlaceholderString dps) {
        String playerName = dps.values.get("player_name");
        if (playerName == null) return "";
        String separator = dps.values.getOrDefault("separator", ", ");

        Set<String> syncedTags = ClientSyncedData.getTags(playerName);
        if (!syncedTags.isEmpty()) {
            return syncedTags.stream().sorted().collect(Collectors.joining(separator));
        }

        ClientWorld level = MinecraftClient.getInstance().world;
        if (level != null) {
            for (AbstractClientPlayerEntity player : level.getPlayers()) {
                if (player.getName().getString().equals(playerName)) {
                    return player.getCommandTags().stream()
                            .sorted()
                            .collect(Collectors.joining(separator));
                }
            }
        }
        return "";
    }

    @Override
    public @Nullable List<String> getValueNames() { return List.of("player_name", "separator"); }

    @Override
    public @NotNull String getDisplayName() { return "Player Tags List"; }

    @Override
    public @Nullable List<String> getDescription() {
        return List.of(
                "Returns a sorted list of all tags on the named player.",
                "Requires Extra Placeholders on the server for full functionality."
        );
    }

    @Override
    public String getCategory() { return "World"; }

    @Override
    public @NotNull DeserializedPlaceholderString getDefaultPlaceholderString() {
        HashMap<String, String> values = new LinkedHashMap<>();
        values.put("player_name", "Steve");
        values.put("separator", ", ");
        return new DeserializedPlaceholderString(this.getIdentifier(), values, "");
    }
}
