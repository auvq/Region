package me.auvq.region.flag.flags;

import lombok.Getter;
import me.auvq.region.flag.Flag;
import me.auvq.region.flag.FlagManager;
import me.auvq.region.region.RegionsManager;
import me.auvq.region.utils.CC;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

public class InteractFlag extends Flag implements Listener {

    public static FlagManager.FlagType type = FlagManager.FlagType.INTERACT;

    public InteractFlag(State state) {
        super(type, state, "Prevents players from interacting with blocks in the region.");
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        if (event.getClickedBlock() == null) {
            return;
        }

        RegionsManager.getRegions().forEach(region -> {
            if(region.getCuboid().isIn(event.getClickedBlock().getLocation())) {
                region.getFlags().forEach(flag -> {
                    if(flag instanceof InteractFlag) {
                        if(event.getPlayer().hasPermission("region.bypass")) return;

                        event.setCancelled(shouldCancel(RegionsManager.getRegion(event.getClickedBlock().getLocation()), event.getPlayer()));

                        if(event.isCancelled()) {
                            event.getPlayer().sendMessage(CC.color("&cYou cannot interact with blocks here!"));
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
