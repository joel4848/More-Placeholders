package com.joel4848.moreplaceholders.client;

import com.joel4848.moreplaceholders.client.data.ClientSyncedData;
import com.joel4848.moreplaceholders.client.placeholder.scoreboard.*;
import com.joel4848.moreplaceholders.client.placeholder.world.*;
import com.joel4848.moreplaceholders.network.ScoreboardSyncPayload;
import com.joel4848.moreplaceholders.network.TagSyncPayload;
import de.keksuccino.fancymenu.customization.placeholder.PlaceholderRegistry;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;

import java.util.*;

public class MorePlaceholdersClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {


        ClientPlayNetworking.registerGlobalReceiver(ScoreboardSyncPayload.ID, (payload, context) -> {

            List<ClientSyncedData.ObjectiveInfo> objectives = new ArrayList<>();
            for (ScoreboardSyncPayload.ObjectiveData o : payload.objectives()) {
                objectives.add(new ClientSyncedData.ObjectiveInfo(
                        o.name(), o.displayName(), o.criteriaName(), o.renderType()
                ));
            }
            List<ClientSyncedData.ScoreEntry> scoreEntries = new ArrayList<>();
            for (ScoreboardSyncPayload.ScoreData s : payload.scores()) {
                scoreEntries.add(new ClientSyncedData.ScoreEntry(
                        s.holderName(), s.objectiveName(), s.value()
                ));
            }

            context.client().execute(() ->
                    ClientSyncedData.updateScoreboard(objectives, scoreEntries));
        });

        ClientPlayNetworking.registerGlobalReceiver(TagSyncPayload.ID, (payload, context) -> {
            Map<String, Set<String>> tagMap = new HashMap<>();
            for (TagSyncPayload.PlayerTagData p : payload.players()) {
                tagMap.put(p.playerName(), p.tags());
            }
            context.client().execute(() -> ClientSyncedData.updateTags(tagMap));
        });

        ClientPlayConnectionEvents.DISCONNECT.register((handler, client) ->
                ClientSyncedData.clear());

        // Give me those placeholders back >:(
        PlaceholderRegistry.register(new ScoreboardScorePlaceholder());
        PlaceholderRegistry.register(new ScoreboardObjectivesListPlaceholder());
        PlaceholderRegistry.register(new ScoreboardTrackedPlayersPlaceholder());
        PlaceholderRegistry.register(new ScoreboardDisplaySlotPlaceholder());
        PlaceholderRegistry.register(new ScoreboardHasScorePlaceholder());
        PlaceholderRegistry.register(new ScoreboardObjectiveDisplayNamePlaceholder());
        PlaceholderRegistry.register(new ScoreboardObjectiveCriteriaPlaceholder());
        PlaceholderRegistry.register(new ScoreboardObjectiveRenderTypePlaceholder());
        PlaceholderRegistry.register(new ScoreboardPlayerScoresListPlaceholder());
        PlaceholderRegistry.register(new ScoreboardObjectiveCountPlaceholder());
        PlaceholderRegistry.register(new ScoreboardPlayerTeamPlaceholder());

        PlaceholderRegistry.register(new PlayerHasTagPlaceholder());
        PlaceholderRegistry.register(new PlayerTagsListPlaceholder());

        PlaceholderRegistry.register(new PlayersWithScorePlaceholder());
    }
}
