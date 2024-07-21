package me.auvq.region.flag;

import lombok.Getter;
import lombok.Setter;
import me.auvq.region.Main;
import me.auvq.region.region.Region;
import me.auvq.region.region.RegionsManager;
import me.auvq.region.utils.CC;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

public abstract class Flag implements Listener {

    public enum State {
        EVERYONE,
        WHITELIST,
        NONE
    }

    private static FlagManager.FlagType type;

    @Getter @Setter
    protected State state;

    @Getter
    protected String description;

    public Flag(FlagManager.FlagType type, State state, String description) {
        this.type = type;
        this.state = state;
        this.description = description;

        Bukkit.getPluginManager().registerEvents(this, Main.getInstance());
    }

    public boolean shouldCancel(Region region, Player player){
//        if(player.hasPermission("region.bypass")) return false;

        switch (state) {
            case EVERYONE:
                return false;
            case WHITELIST:
                return region.getAllowedPlayers().contains(player.getUniqueId());
            case NONE:
                return true;
        }
        return false;
    }

    public FlagManager.FlagType getType() {
        return type;
    }
}