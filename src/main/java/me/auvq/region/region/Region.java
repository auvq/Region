package me.auvq.region.region;

import lombok.Getter;
import me.auvq.region.Main;
import me.auvq.region.flag.Flag;
import me.auvq.region.flag.FlagManager;
import me.auvq.region.utils.spider.SpiderCuboid;
import me.auvq.region.utils.spider.SpiderLocation;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

public class Region {

    @Getter
    private String name;

    @Getter
    private SpiderCuboid cuboid;

    @Getter
    private final List<UUID> allowedPlayers;

    @Getter
    private final List<Flag> flags;

    public Region(String name, SpiderCuboid cuboid) {
        this.name = name;
        this.cuboid = cuboid;

        this.allowedPlayers = new ArrayList<>();

        this.flags = FlagManager.createAllFlagInstances();

        Main.getInstance().getCollection().insertOne(serialize());
    }

    public Region(Document doc) {
        this.name = doc.getString("RegionName");
        this.cuboid = deserializeCuboid(doc.get("Cuboid", Document.class));

        this.allowedPlayers = doc.getList("allowedPlayers", String.class).stream().map(UUID::fromString).collect(Collectors.toList());
        this.flags = doc.getList("flags", String.class).stream().map(flagString -> {
            String[] parts = flagString.split(" - ");
            String flagName = parts[0];
            Flag.State flagState = Flag.State.valueOf(parts[1]);
            Flag flag = FlagManager.createFlagInstance(FlagManager.FlagType.valueOf(flagName));
            if (flag != null) {
                flag.setState(flagState);
            }
            return flag;
        }).filter(Objects::nonNull).collect(Collectors.toList());
    }

    public SpiderCuboid deserializeCuboid(Document doc) {
        return new SpiderCuboid(
                new SpiderLocation(doc.get("Corner1", Document.class)),
                new SpiderLocation(doc.get("Corner2", Document.class))
        );
    }

    public Document serializeCuboid(SpiderCuboid cuboid) {
        Document doc = new Document()
                .append("Corner1", new Document("world", cuboid.getFirst().getWorld())
                        .append("x", cuboid.getFirst().getX())
                        .append("y", cuboid.getFirst().getY())
                        .append("z", cuboid.getFirst().getZ()))
                .append("Corner2", new Document("world", cuboid.getSecond().getWorld())
                        .append("x", cuboid.getSecond().getX())
                        .append("y", cuboid.getSecond().getY())
                        .append("z", cuboid.getSecond().getZ()));
        return doc;
    }

    public Document serialize() {
        List<String> playerUUIDs = this.allowedPlayers.stream().map(UUID::toString).collect(Collectors.toList());
        List<String> flagStates = this.flags.stream()
                .map(flag -> flag.getType().name() + " - " + flag.getState().name())
                .collect(Collectors.toList());

        Document regionDoc = new Document("RegionName", this.name)
                .append("Cuboid", serializeCuboid(this.cuboid))
                .append("allowedPlayers", playerUUIDs)
                .append("flags", flagStates);

        return regionDoc;
    }

    private void updateData(String oldName, String newName) {
        Document query = new Document("RegionName", oldName);
        Document update = new Document("$set", new Document("RegionName", newName));

        try {
            Main.getInstance().getCollection().updateOne(query, update);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updateData() {
        Document serializedRegion = this.serialize();
        Document query = new Document("RegionName", this.getName());

        try {
            Main.getInstance().getCollection().updateOne(query, new Document("$set", serializedRegion));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private final void addFlag(Flag flag) {
        this.flags.add(flag);
        updateData();
    }

    private void removeFlag(Flag flag) {
        this.flags.remove(flag);
        updateData();
    }

    public final boolean hasFlag(Flag flag) {
        return this.flags.contains(flag);
    }

    public final void addPlayer(UUID player) {
        this.allowedPlayers.add(player);
        updateData();
    }

    public final void removePlayer(UUID player) {
        this.allowedPlayers.remove(player);
        updateData();
    }

    public final boolean hasPlayer(UUID player) {
        updateData();
        return this.allowedPlayers.contains(player);
    }

    public final void setCuboid(SpiderCuboid cuboid) {
        this.cuboid = cuboid;
        updateData();
    }

    public final void setName(String newName) {
        String oldName = this.name;
        this.name = newName;

        updateData(oldName, newName);
    }
}
