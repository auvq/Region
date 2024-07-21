package me.auvq.region.flag.flags;

import me.auvq.region.flag.Flag;
import me.auvq.region.flag.FlagManager;
import me.auvq.region.region.RegionsManager;
import me.auvq.region.utils.CC;
import me.auvq.region.utils.MsgUtil;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

public class BlockBreakFlag extends Flag implements Listener {

    public static FlagManager.FlagType type = FlagManager.FlagType.BLOCK_BREAK;

    public BlockBreakFlag(State state) {
        super(type, state,"Prevents players from breaking blocks in the region.");
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event){
        RegionsManager.getRegions().forEach(region -> {
            if(region.getCuboid().isIn(event.getBlock().getLocation())) {
                region.getFlags().forEach(flag -> {
                    if (flag instanceof BlockBreakFlag) {

                        if(event.getPlayer().hasPermission("region.bypass")) return;

                        event.setCancelled(shouldCancel(RegionsManager.getRegion(event.getBlock().getLocation()), event.getPlayer()));

                        if (event.isCancelled()) {
                            event.getPlayer().sendMessage(CC.color("&cYou cannot break blocks here!"));
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
