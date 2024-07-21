package me.auvq.region.utils.spider;


import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

@Getter
@AllArgsConstructor
public class SpiderCuboid {

    private final int minX,maxX;
    private final int minY,maxY;
    private final int minZ,maxZ;

    private final double minXCentered,maxXCentered;
    private final double minYCentered,maxYCentered;
    private final double minZCentered,maxZCentered;

    private final World world;

    public SpiderCuboid(@NotNull final SpiderLocation first, @NotNull final SpiderLocation second){
        this.minX = Math.min(first.getBlockX(), second.getBlockX());
        this.maxX = Math.max(first.getBlockX(), second.getBlockX());

        this.minY = Math.min(first.getBlockY(), second.getBlockY());
        this.maxY = Math.max(first.getBlockY(), second.getBlockY());

        this.minZ = Math.min(first.getBlockZ(), second.getBlockZ());
        this.maxZ = Math.max(first.getBlockZ(), second.getBlockZ());

        this.world = first.getBukkitWorld();

        this.minXCentered = this.minX + 0.5;
        this.maxXCentered = this.maxX + 0.5;

        this.minYCentered = this.minY + 0.5;
        this.maxYCentered = this.maxY + 0.5;

        this.minZCentered = this.minZ + 0.5;
        this.maxZCentered = this.maxZ + 0.5;
    }

    public final boolean isIn(@NotNull final SpiderLocation parent){

        return new MergedBoolean<SpiderLocation>()
                .add(location -> location.getBukkitWorld() == this.world)
                .add(location -> location.getBlockX() >= this.minX)
                .add(location -> location.getBlockX() <= this.maxX)
                .add(location -> location.getBlockY() >= this.minY)
                .add(location -> location.getBlockY() <= this.maxY)
                .add(location -> location.getBlockZ() >= this.minZ)
                .add(location -> location.getBlockZ() <= this.maxZ)
                .build(parent);
    }

    public final boolean isIn(@NotNull final Location location){
        return this.isIn(SpiderLocation.from(location));
    }

    public final boolean isIn(@NotNull final Player player){
        return this.isIn(player.getLocation());
    }

    public final boolean isInWithMarge(@NotNull final SpiderLocation parent, final double marge){
        return new MergedBoolean<SpiderLocation>()
                .add(location -> location.getBukkitWorld() == this.getWorld())
                .add(location -> location.getX() >= this.minXCentered - marge)
                .add(location -> location.getX() <= this.maxXCentered + marge)
                .add(location -> location.getY() >= this.minYCentered - marge)
                .add(location -> location.getY() <= this.maxYCentered + marge)
                .add(location -> location.getZ() >= this.minZCentered - marge)
                .add(location -> location.getZ() <= this.maxZCentered + marge)
                .build(parent);
    }

    public final boolean isInWithMarge(@NotNull final Location location, final double marge){
        return this.isInWithMarge(SpiderLocation.from(location), marge);
    }

    @NotNull
    public Iterator<Block> blockList(){
        final List<Block> blocks = new ArrayList<>(this.getTotalBlockSize());

        for(int x = this.minX; x <= this.maxX; ++x) {
            for(int y = this.minY; y <= this.maxY; ++y) {
                for(int z = this.minZ; z <= this.maxZ; ++z) {
                    final Block block = this.world.getBlockAt(x, y, z);

                    blocks.add(block);
                }
            }
        }

        return blocks.iterator();
    }

    @NotNull
    public SpiderLocation getCenter(){
        return SpiderLocation.from(
                new Location(this.world,
                        (this.maxX - this.minX) / 2 + this.minX,
                        (this.maxY - this.minY) / 2 + this.minY,
                        (this.maxZ - this.minZ) / 2 + this.minZ)
        );
    }

    @NotNull
    public SpiderLocation getFirst(){
        return SpiderLocation.from(
                new Location(this.world, this.minX, this.minY, this.minZ)
        );
    }

    @NotNull
    public SpiderLocation getSecond(){
        return SpiderLocation.from(
                new Location(this.world, this.maxX, this.maxY, this.maxZ)
        );
    }

    public final double getDistance(){
        return this.getFirst().getDistanceTo(this.getSecond());
    }

    public final double getGroundDistance(){
        return this.getFirst().getGroundDistanceTo(this.getSecond());
    }

    public final int getHeight(){
        return this.maxY - this.minY + 1;
    }

    @NotNull
    public final SpiderLocation getRandomLocation(){
        final Random rand = new Random();

        final int x = rand.nextInt(Math.abs(this.maxX - this.minX) + 1) + this.minX;
        final int y = rand.nextInt(Math.abs(this.maxY - this.minY) + 1) + this.minY;
        final int z = rand.nextInt(Math.abs(this.maxZ - this.minZ) + 1) + this.minZ;

        return SpiderLocation.from(
                new Location(this.world, x, y, z)
        );
    }

    public final int getTotalBlockSize() {
        return this.getHeight() * this.getXWidth() * this.getZWidth();
    }

    public final int getXWidth(){
        return this.maxX - this.minX + 1;
    }

    public final int getZWidth() {
        return this.maxZ - this.minZ + 1;
    }


}