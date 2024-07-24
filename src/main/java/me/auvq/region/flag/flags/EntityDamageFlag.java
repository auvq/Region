package me.auvq.region.flag.flags;

import me.auvq.region.Main;
import me.auvq.region.flag.Flag;
import me.auvq.region.flag.FlagManager;
import me.auvq.region.region.RegionsManager;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class EntityDamageFlag extends Flag implements Listener {

    public static FlagManager.FlagType type = FlagManager.FlagType.ENTITY_DAMAGE;

    public EntityDamageFlag(State state) {
        super(type, state, "Prevents players from damaging entities in the region.");
    }

    @EventHandler
    public void onEntityDamage(EntityDamageByEntityEvent event) {
        Main.getInstance().getRegionsManager().getRegions().forEach(region -> {
            if(region.getCuboid().isIn(event.getEntity().getLocation())) {
                region.getFlags().forEach(flag -> {
                    if(flag instanceof EntityDamageFlag) {
                        if(event.getDamager().hasPermission("region.bypass")) return;

                        event.setCancelled(shouldCancel(Main.getInstance().getRegionsManager().getRegion(event.getEntity().getLocation()), (Player) event.getDamager()));

                        if(event.isCancelled()) {
                            event.getDamager().sendMessage("You cannot damage entities here!");
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
