package com.joel4848.moreplaceholders.network;

import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public record TagSyncPayload(List<PlayerTagData> players) implements CustomPayload {

    public static final CustomPayload.Id<TagSyncPayload> ID =
            new CustomPayload.Id<>(Identifier.of("more-placeholders", "tag_sync"));

    public static final PacketCodec<PacketByteBuf, TagSyncPayload> CODEC =
            PacketCodec.of(TagSyncPayload::write, TagSyncPayload::read);

    @Override
    public CustomPayload.Id<? extends CustomPayload> getId() {
        return ID;
    }

    private void write(PacketByteBuf buf) {
        buf.writeInt(players.size());
        for (PlayerTagData p : players) {
            buf.writeString(p.playerName());
            List<String> tagList = new ArrayList<>(p.tags());
            buf.writeInt(tagList.size());
            for (String tag : tagList) {
                buf.writeString(tag);
            }
        }
    }

    private static TagSyncPayload read(PacketByteBuf buf) {
        int count = buf.readInt();
        List<PlayerTagData> players = new ArrayList<>(count);
        for (int i = 0; i < count; i++) {
            String name = buf.readString();
            int tagCount = buf.readInt();
            Set<String> tags = new HashSet<>(tagCount);
            for (int j = 0; j < tagCount; j++) {
                tags.add(buf.readString());
            }
            players.add(new PlayerTagData(name, tags));
        }
        return new TagSyncPayload(players);
    }

    public record PlayerTagData(String playerName, Set<String> tags) {}
}
