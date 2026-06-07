package com.joel4848.moreplaceholders.client.placeholder.scoreboard;

import de.keksuccino.fancymenu.customization.placeholder.DeserializedPlaceholderString;
import de.keksuccino.fancymenu.customization.placeholder.Placeholder;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.scoreboard.ScoreboardDisplaySlot;
import net.minecraft.scoreboard.ScoreboardObjective;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

public class ScoreboardDisplaySlotPlaceholder extends Placeholder {

    public ScoreboardDisplaySlotPlaceholder() {
        super("scoreboard_display_slot");
    }

    @Override
    public boolean canRunAsync() { return false; }

    @Override
    public String getReplacementFor(DeserializedPlaceholderString dps) {
        String slotName = dps.values.get("slot");
        if (slotName == null) return "";

        ClientPlayNetworkHandler conn = MinecraftClient.getInstance().getNetworkHandler();
        if (conn == null) return "";

        ScoreboardDisplaySlot slot = ScoreboardDisplaySlot.CODEC.byId(slotName);
        if (slot == null) return "";

        ScoreboardObjective obj = conn.getScoreboard().getObjectiveForSlot(slot);
        return obj != null ? obj.getName() : "";
    }

    @Override
    public @Nullable List<String> getValueNames() { return List.of("slot"); }

    @Override
    public @NotNull String getDisplayName() { return "Scoreboard Display Slot"; }

    @Override
    public @Nullable List<String> getDescription() {
        return List.of(
                "Returns the name of the objective displayed in the given slot (e.g. 'sidebar').",
                "Requires Extra Placeholders on the server for full functionality."
        );
    }

    @Override
    public String getCategory() { return "Scoreboard"; }

    @Override
    public @NotNull DeserializedPlaceholderString getDefaultPlaceholderString() {
        HashMap<String, String> values = new LinkedHashMap<>();
        values.put("slot", "sidebar");
        return new DeserializedPlaceholderString(this.getIdentifier(), values, "");
    }
}
