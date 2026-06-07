package com.joel4848.moreplaceholders.network;

import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.List;

public record ScoreboardSyncPayload(
        List<ObjectiveData> objectives,
        List<ScoreData> scores
) implements CustomPayload {

    public static final CustomPayload.Id<ScoreboardSyncPayload> ID =
            new CustomPayload.Id<>(Identifier.of("more-placeholders", "scoreboard_sync"));

    public static final PacketCodec<PacketByteBuf, ScoreboardSyncPayload> CODEC =
            PacketCodec.of(ScoreboardSyncPayload::write, ScoreboardSyncPayload::read);

    @Override
    public CustomPayload.Id<? extends CustomPayload> getId() {
        return ID;
    }

    private void write(PacketByteBuf buf) {
        buf.writeInt(objectives.size());
        for (ObjectiveData obj : objectives) {
            buf.writeString(obj.name());
            buf.writeString(obj.displayName());
            buf.writeString(obj.criteriaName());
            buf.writeString(obj.renderType());
        }
        buf.writeInt(scores.size());
        for (ScoreData s : scores) {
            buf.writeString(s.holderName());
            buf.writeString(s.objectiveName());
            buf.writeInt(s.value());
        }
    }

    private static ScoreboardSyncPayload read(PacketByteBuf buf) {
        int objCount = buf.readInt();
        List<ObjectiveData> objectives = new ArrayList<>(objCount);
        for (int i = 0; i < objCount; i++) {
            objectives.add(new ObjectiveData(
                    buf.readString(),
                    buf.readString(),
                    buf.readString(),
                    buf.readString()
            ));
        }
        int scoreCount = buf.readInt();
        List<ScoreData> scores = new ArrayList<>(scoreCount);
        for (int i = 0; i < scoreCount; i++) {
            scores.add(new ScoreData(
                    buf.readString(),
                    buf.readString(),
                    buf.readInt()
            ));
        }
        return new ScoreboardSyncPayload(objectives, scores);
    }

    public record ObjectiveData(String name, String displayName, String criteriaName, String renderType) {}
    public record ScoreData(String holderName, String objectiveName, int value) {}
}
