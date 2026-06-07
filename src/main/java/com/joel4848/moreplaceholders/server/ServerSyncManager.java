package com.joel4848.moreplaceholders.server;

import com.joel4848.moreplaceholders.network.ScoreboardSyncPayload;
import com.joel4848.moreplaceholders.network.TagSyncPayload;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.scoreboard.ScoreboardEntry;
import net.minecraft.scoreboard.ScoreboardObjective;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;

import java.util.*;

public class ServerSyncManager {

    public static void syncToPlayer(ServerPlayerEntity player) {
        MinecraftServer server = player.getServer();
        if (server == null) return;

        sendScoreboardSync(player, server);
        sendTagSync(player, server);
    }

    public static void broadcastScoreboardSync(MinecraftServer server) {
        for (ServerPlayerEntity player : server.getPlayerManager().getPlayerList()) {
            sendScoreboardSync(player, server);
        }
    }

    public static void broadcastTagSync(MinecraftServer server) {
        for (ServerPlayerEntity player : server.getPlayerManager().getPlayerList()) {
            sendTagSync(player, server);
        }
    }

    private static void sendScoreboardSync(ServerPlayerEntity player, MinecraftServer server) {
        if (!ServerPlayNetworking.canSend(player, ScoreboardSyncPayload.ID)) return;

        Scoreboard scoreboard = server.getScoreboard();

        List<ScoreboardSyncPayload.ObjectiveData> objectives = new ArrayList<>();
        for (ScoreboardObjective obj : scoreboard.getObjectives()) {
            objectives.add(new ScoreboardSyncPayload.ObjectiveData(
                    obj.getName(),
                    obj.getDisplayName().getString(),
                    obj.getCriterion().getName(),
                    obj.getRenderType().getName()
            ));
        }

        List<ScoreboardSyncPayload.ScoreData> scores = new ArrayList<>();
        for (ScoreboardObjective obj : scoreboard.getObjectives()) {
            for (ScoreboardEntry entry : scoreboard.getScoreboardEntries(obj)) {
                scores.add(new ScoreboardSyncPayload.ScoreData(
                        entry.owner(),
                        obj.getName(),
                        entry.value()
                ));
            }
        }

        ServerPlayNetworking.send(player, new ScoreboardSyncPayload(objectives, scores));
    }

    private static void sendTagSync(ServerPlayerEntity player, MinecraftServer server) {
        if (!ServerPlayNetworking.canSend(player, TagSyncPayload.ID)) return;

        List<TagSyncPayload.PlayerTagData> playerData = new ArrayList<>();
        for (ServerPlayerEntity p : server.getPlayerManager().getPlayerList()) {
            playerData.add(new TagSyncPayload.PlayerTagData(
                    p.getName().getString(),
                    new HashSet<>(p.getCommandTags())
            ));
        }

        ServerPlayNetworking.send(player, new TagSyncPayload(playerData));
    }
}
