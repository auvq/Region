package me.auvq.region.listeners;

import lombok.Getter;
import me.auvq.region.Main;
import me.auvq.region.region.Region;
import me.auvq.region.utils.CC;
import me.auvq.region.utils.ItemBuilder;
import me.auvq.region.utils.spider.SpiderCuboid;
import me.auvq.region.utils.spider.SpiderLocation;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class WandListener implements Listener {

    @Getter
    private final List<UUID> editMode = new ArrayList<>();

    private final HashMap<UUID, Location> firstClicks = new HashMap<>();

    @Getter
    private final Map<UUID, Region> playerEditingRegions = new HashMap<>();

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if(event.getItem() == null || event.getItem().getType() != Material.BLAZE_ROD) return;
        if(!editMode.contains(player.getUniqueId())) return;
        if(event.getClickedBlock() == null || event.getClickedBlock().getType() == Material.AIR) return;

        if(firstClicks.containsKey(player.getUniqueId())) {
            Location firstLocation = firstClicks.remove(player.getUniqueId());
            Location secondLocation = event.getClickedBlock().getLocation();

            toggleWand(player);

            SpiderCuboid regionCuboid = new SpiderCuboid(
                    SpiderLocation.from(firstLocation), SpiderLocation.from(secondLocation));

            if(getPlayerEditingRegions().get(player.getUniqueId()) == null){
                Main.getInstance().getRegionsManager().addRegion(new Region(CC.color("&e" + player.getName() + "'s &6Region"), regionCuboid));
            } else {
                getPlayerEditingRegions().get(player.getUniqueId()).setCuboid(regionCuboid);
            }

            player.sendMessage(CC.color("&eFinished setting up the region!"));

            event.setCancelled(true);
        } else {
            firstClicks.put(player.getUniqueId(), event.getClickedBlock().getLocation());
            player.sendMessage(CC.color("&eFirst corner set! Please right click the second corner!"));
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onDrop(PlayerDropItemEvent event) {
        ItemStack editWand = ItemBuilder.from(Material.BLAZE_ROD).name("&eRegion Wand").lore("", "&7Right click to select region").build();
        Player player = event.getPlayer();

        if(!event.getItemDrop().getItemStack().isSimilar(editWand)) return;
        if(!editMode.contains(player.getUniqueId())) return;

        event.setCancelled(true);
    }

    public void toggleWand(Player player) {
        ItemStack editWand = ItemBuilder.from(Material.BLAZE_ROD).name("&eRegion Wand").lore("", "&7Right click to select region").build();
        if(editMode.contains(player.getUniqueId())) {
            editMode.remove(player.getUniqueId());

            if(player.getInventory().contains(editWand)) player.getInventory().remove(editWand);

            player.sendMessage( CC.color("&eEdit mode &cdisabled&e."));
        } else {
            editMode.add(player.getUniqueId());
            player.getInventory().addItem(editWand);
            player.sendMessage(CC.color("&eEdit mode &aenabled&e."));
        }
    }
    public void toggleEditMode(Player player, Region region) {
        playerEditingRegions.put(player.getUniqueId(), region);
        ItemStack editWand = ItemBuilder.from(Material.BLAZE_ROD).name("&eRegion Wand").lore("", "&7Right click to select region").build();
        if(editMode.contains(player.getUniqueId())) {
            editMode.remove(player.getUniqueId());

            if(player.getInventory().contains(editWand)) player.getInventory().remove(editWand);

            player.sendMessage( CC.color("&eEdit mode &cdisabled&e."));
        } else {
            editMode.add(player.getUniqueId());
            player.getInventory().addItem(editWand);
            player.sendMessage(CC.color("&eEdit mode &aenabled&e."));
        }
    }
}
