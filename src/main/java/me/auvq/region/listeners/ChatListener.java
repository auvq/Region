package me.auvq.region.listeners;

import io.papermc.paper.event.player.AsyncChatEvent;
import me.auvq.region.region.Region;
import me.auvq.region.utils.CC;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.*;

public class ChatListener implements Listener {

    public Map<UUID, Region> renameMode = new HashMap<>();

    public Map<UUID, Region> addPlayerMode = new HashMap<>();

    public Map<UUID, Region> removePlayerMode = new HashMap<>();

    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        if(!renameMode.containsKey(player.getUniqueId()) &&
           !addPlayerMode.containsKey(player.getUniqueId()) &&
           !removePlayerMode.containsKey(player.getUniqueId())) return;

        event.setCancelled(true);
        if(renameMode.containsKey(player.getUniqueId())) {
            Region region = renameMode.remove(player.getUniqueId());
            region.setName(event.getMessage());
            player.sendMessage(CC.color("&eSuccessfully renamed the region to &6") + event.getMessage());
            return;
        }

        if(addPlayerMode.containsKey(player.getUniqueId())) {
            Region region = addPlayerMode.remove(player.getUniqueId());
            if(Bukkit.getPlayer(event.getMessage()) == null) {
                player.sendMessage(CC.color("&cPlayer not found! Make sure the player is online!"));
                return;
            }

            region.addPlayer(Bukkit.getPlayerUniqueId(event.getMessage()));

            player.sendMessage(CC.color("&eSuccessfully added &6" + Bukkit.getPlayer(event.getMessage()) + " &eto the region's whitelist"));
            return;
        }

        if(removePlayerMode.containsKey(player.getUniqueId())) {
            Region region = removePlayerMode.remove(player.getUniqueId());

            if(!region.getAllowedPlayers().contains(Bukkit.getPlayerUniqueId(event.getMessage()))) {
                player.sendMessage(CC.color("&cPlayer not found in the region's whitelist!"));
                return;
            }

            region.removePlayer(Bukkit.getPlayerUniqueId(event.getMessage()));

            player.sendMessage(CC.color("&eSuccessfully removed &6" + Bukkit.getPlayer(event.getMessage()) + " &efrom the region's whitelist"));
            return;
        }
    }
}
