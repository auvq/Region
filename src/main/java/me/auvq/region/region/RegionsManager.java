package me.auvq.region.region;

import lombok.Getter;
import me.auvq.region.Main;
import org.bukkit.Location;

import java.util.ArrayList;
import java.util.List;

public class RegionsManager {

    //I'm sorry for adding an s (Region(S)), I had some issue with the name RegionManager somewhy, so I had to change it..



    @Getter
    private final List<Region> regions = new ArrayList<>();

    private final Main plugin = Main.getInstance();

    public RegionsManager(){
        loadRegions();
    }

    public void loadRegions() {
        regions.clear();
        plugin.getCollection().find().forEach(doc -> regions.add(new Region(doc)));
    }

    public void addRegion(Region region) {
        regions.add(region);
    }

    public void removeRegion(Region region) {
        regions.remove(region);
    }

    public Region getRegion(String name) {
        for (Region region : regions) {
            if (region.getName().equalsIgnoreCase(name)) {
                return region;
            }
        }
        return null;
    }

    public Region getRegion(Location location){
        for (Region region : regions) {
            if (region.getCuboid().isIn(location)) {
                return region;
            }
        }
        return null;
    }

}
