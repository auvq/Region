package me.auvq.region.region;

import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import lombok.Getter;
import me.auvq.region.Main;
import me.auvq.region.utils.CC;
import me.auvq.region.utils.MsgUtil;
import org.bson.Document;
import org.bukkit.Location;

import java.util.ArrayList;
import java.util.List;

public class RegionsManager {

    //I'm sorry for adding an s (Region(S)), I had some issue with the name RegionManager somewhy, so I had to change it..

    @Getter
    private static final List<Region> regions = new ArrayList<>();

    private static final Main plugin = Main.getInstance();

    public static void loadRegions() {
        regions.clear();
        plugin.getCollection().find().forEach(doc -> regions.add(new Region(doc)));
    }

    public static void addRegion(Region region) {
        regions.add(region);
    }

    public static void removeRegion(Region region) {
        regions.remove(region);
    }

    public static Region getRegion(String name) {
        for (Region region : regions) {
            if (region.getName().equalsIgnoreCase(name)) {
                return region;
            }
        }
        return null;
    }

    public static Region getRegion(Location location){
        for (Region region : regions) {
            if (region.getCuboid().isIn(location)) {
                return region;
            }
        }
        return null;
    }

}
