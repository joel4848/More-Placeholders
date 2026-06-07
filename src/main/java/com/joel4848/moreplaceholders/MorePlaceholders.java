package com.joel4848.moreplaceholders;

import com.joel4848.moreplaceholders.network.ScoreboardSyncPayload;
import com.joel4848.moreplaceholders.network.TagSyncPayload;
import com.joel4848.moreplaceholders.server.ServerSyncManager;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MorePlaceholders implements ModInitializer {
    public static final String MOD_ID = "more-placeholders";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    private static final int SYNC_INTERVAL_TICKS = 20; // every second
    private int tickCounter = 0;

    @Override
    public void onInitialize() {
        PayloadTypeRegistry.playS2C().register(ScoreboardSyncPayload.ID, ScoreboardSyncPayload.CODEC);
        PayloadTypeRegistry.playS2C().register(TagSyncPayload.ID, TagSyncPayload.CODEC);

        ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> {
            server.execute(() -> ServerSyncManager.syncToPlayer(handler.player));
        });

        ServerTickEvents.END_SERVER_TICK.register(server -> {
            tickCounter++;
            if (tickCounter >= SYNC_INTERVAL_TICKS) {
                tickCounter = 0;
                ServerSyncManager.broadcastScoreboardSync(server);
                ServerSyncManager.broadcastTagSync(server);
            }
        });

        LOGGER.info("[MorePlaceholders] Initialised.");
    }
}
