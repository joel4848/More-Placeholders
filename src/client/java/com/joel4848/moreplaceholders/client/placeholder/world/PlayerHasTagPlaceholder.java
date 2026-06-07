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

public class PlayerHasTagPlaceholder extends Placeholder {

    public PlayerHasTagPlaceholder() {
        super("player_has_tag");
    }

    @Override
    public boolean canRunAsync() { return false; }

    @Override
    public String getReplacementFor(DeserializedPlaceholderString dps) {
        String playerName = dps.values.get("player_name");
        String tagName = dps.values.get("tag_name");
        if (playerName == null || tagName == null) return "false";

        if (!ClientSyncedData.getTags(playerName).isEmpty()
                || ClientSyncedData.getObjectives().isEmpty() ) {

            ClientWorld level = MinecraftClient.getInstance().world;
            if (level != null) {
                for (AbstractClientPlayerEntity player : level.getPlayers()) {
                    if (player.getName().getString().equals(playerName)) {
                        return String.valueOf(ClientSyncedData.hasTag(playerName, tagName));
                    }
                }
            }
            return String.valueOf(ClientSyncedData.hasTag(playerName, tagName));
        }

        ClientWorld level = MinecraftClient.getInstance().world;
        if (level != null) {
            for (AbstractClientPlayerEntity player : level.getPlayers()) {
                if (player.getName().getString().equals(playerName)) {
                    return String.valueOf(player.getCommandTags().contains(tagName));
                }
            }
        }
        return "false";
    }

    @Override
    public @Nullable List<String> getValueNames() { return List.of("player_name", "tag_name"); }

    @Override
    public @NotNull String getDisplayName() { return "Player Has Tag"; }

    @Override
    public @Nullable List<String> getDescription() {
        return List.of(
                "Returns 'true' if the named player has the given tag.",
                "Requires Extra Placeholders on the server for full functionality."
        );
    }

    @Override
    public String getCategory() { return "World"; }

    @Override
    public @NotNull DeserializedPlaceholderString getDefaultPlaceholderString() {
        HashMap<String, String> values = new LinkedHashMap<>();
        values.put("player_name", "Steve");
        values.put("tag_name", "my_tag");
        return new DeserializedPlaceholderString(this.getIdentifier(), values, "");
    }
}
