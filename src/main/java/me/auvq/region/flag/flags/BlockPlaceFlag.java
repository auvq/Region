package me.auvq.region.flag.flags;

import me.auvq.region.flag.Flag;
import me.auvq.region.flag.FlagManager;
import me.auvq.region.region.RegionsManager;
import me.auvq.region.utils.CC;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;

public class BlockPlaceFlag extends Flag implements Listener {

    public static FlagManager.FlagType type = FlagManager.FlagType.BLOCK_PLACE;

    public BlockPlaceFlag(State state) {
        super(type, state, "Prevents players from placing blocks in the region.");
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event){
        RegionsManager.getRegions().forEach(region -> {
            if(region.getCuboid().isIn(event.getBlock().getLocation())) {
                region.getFlags().forEach(flag -> {
                    if (flag instanceof BlockPlaceFlag) {

                        if(event.getPlayer().hasPermission("region.bypass")) return;

                        event.setCancelled(shouldCancel(RegionsManager.getRegion(event.getBlock().getLocation()), event.getPlayer()));

                        if (event.isCancelled()) {
                            event.getPlayer().sendMessage(CC.color("&cYou cannot place blocks here!"));
                        }
                    }
                });
            }
        });
    }

    @Override
    public FlagManager.FlagType getType() {
        return type;
    }
}
