package me.auvq.region.utils.spider;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.bson.Document;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.util.NumberConversions;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Getter
@Setter
@AllArgsConstructor
public class SpiderLocation {

    private static final Pattern PATTERN = Pattern.compile("(?<world>[^/]+):(?<x>[\\-0-9.]+),(?<y>[\\-0-9.]+),(?<z>[\\-0-9.]+)(:(?<yaw>[\\-0-9.]+):(?<pitch>[\\-0-9.]+))?");

    private String world;

    private double x,y,z;
    private float yaw,pitch;

    public static SpiderLocation empty(){
        return new SpiderLocation("world", 0, 0, 0, 0.0f, 0.0f);
    }

    public static SpiderLocation from(@NotNull final Location location){
        return new SpiderLocation(location.getWorld().getName(),
                location.getX(), location.getY(), location.getZ(),
                location.getYaw(), location.getPitch());
    }

    public static SpiderLocation from(@NotNull final String string){
        final Matcher match = PATTERN.matcher(string
                .replace("_", ".")
                .replace("/", ":"));

        if (!match.matches())
            return SpiderLocation.empty();

        return new SpiderLocation(
                match.group("world"),
                NumberConversions.toDouble(match.group("x")),
                NumberConversions.toDouble(match.group("y")),
                NumberConversions.toDouble(match.group("z")),
                Optional.ofNullable(match.group("yaw"))
                        .map(NumberConversions::toFloat)
                        .orElse(0.0f),
                Optional.ofNullable(match.group("pitch"))
                        .map(NumberConversions::toFloat)
                        .orElse(0.0f)
        );
    }

    public SpiderLocation(final double x, final double y, final double z) {
        this(x, y, z, 0.0F, 0.0F);
    }

    public SpiderLocation(@NotNull final String world, final double x, final double y, final double z) {
        this(world, x, y, z, 0.0F, 0.0F);
    }

    public SpiderLocation(final Location location) {
        this(String.valueOf(location.getWorld()), location.getX(), location.getY(), location.getZ(), 0.0F, 0.0F);
    }

    public SpiderLocation(Document doc){
        this(doc.getString("world"), doc.getDouble("x"), doc.getDouble("y"), doc.getDouble("z"));
    }

    public SpiderLocation(final double x, final double y, final double z, final float yaw, final float pitch) {
        this("world", x, y, z, yaw, pitch);
    }

    public int getBlockX(){
        return this.getBukkitLocation().getBlockX();
    }

    public int getBlockY(){
        return this.getBukkitLocation().getBlockY();
    }

    public int getBlockZ(){
        return this.getBukkitLocation().getBlockZ();
    }

    public double getGroundDistanceTo(@NotNull final SpiderLocation location) {
        return Math.sqrt(
                Math.pow(this.x - location.x, 2) + Math.pow(this.z - location.z, 2)
        );
    }

    public double getDistanceTo(@NotNull final SpiderLocation location) {
        return Math.sqrt(
                Math.pow(this.x - location.x, 2) + Math.pow(this.y - location.y, 2) + Math.pow(this.z - location.z, 2)
        );
    }

    @NotNull
    public List<SpiderLocation> getCircle(double radius, int amount){
        final double increment = (2 * Math.PI) / amount;

        final List<SpiderLocation> locations = new ArrayList<>();

        for (int i = 0; i < amount; i++){
            double angle = i * increment;

            double x = this.x + (radius * Math.cos(angle));
            double z = this.z + (radius * Math.sin(angle));

            locations.add(
                    new SpiderLocation(
                            this.world,
                            x,
                            this.y,
                            z
                    )
            );
        }

        return locations;
    }

    @NotNull
    public String locationToString(){
        String s = getBukkitWorld().getName() + ':';

        s += String.format(
                Locale.ENGLISH,
                "%.2f,%.2f,%.2f",
                this.x, this.y, this.z);

        if (this.yaw != 0.0f || this.pitch != 0.0f){
            s += String.format(
                    Locale.ENGLISH,
                    ":%.2f:%.2f",
                    this.yaw, this.pitch);
        }

        return s.replace(":", "/")
                .replace(".", "_");
    }

    @NotNull
    public Location getBukkitLocation() {
        return new Location(
                this.getBukkitWorld(),
                this.x, this.y, this.z,
                this.yaw, this.pitch
        );
    }

    public World getBukkitWorld() {
        final World world = Bukkit.getWorld(this.world);

        return world;
    }


    @Override
    public boolean equals(@NotNull final Object object) {
        if (!(object instanceof SpiderLocation location))
            return false;

        return location.x == this.x && location.y == this.y && location.z == this.z
                && location.pitch == this.pitch && location.yaw == this.yaw;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("x", this.x)
                .append("y", this.y)
                .append("z", this.z)
                .append("yaw", this.yaw)
                .append("pitch", this.pitch)
                .append("world", this.world)
                .toString();
    }

}