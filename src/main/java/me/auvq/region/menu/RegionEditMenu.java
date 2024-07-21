package me.auvq.region.menu;

import me.auvq.region.listeners.ChatListener;
import me.auvq.region.listeners.WandListener;
import me.auvq.region.region.Region;
import me.auvq.region.utils.CC;
import me.auvq.region.utils.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.mineacademy.fo.menu.Menu;
import org.mineacademy.fo.menu.button.Button;
import org.mineacademy.fo.menu.button.annotation.Position;

import java.util.HashMap;
import java.util.Map;

public class RegionEditMenu extends Menu {

    @Position(9 * 3 - 8)
    private Button rename;

    @Position(9 * 3 - 6)
    private Button addPlayer;

    @Position(9 * 3 - 4)
    private Button removePlayer;

    @Position(9 * 3 - 2)
    private Button redefineLocation;

    @Position(9 * 5 - 5)
    private Button editFlags; // OPENS A GUI WITH ALL FLAGS, TO EDIT THEM (SWITCH THEIR STATE, ENABLE/DISABLE)



    public RegionEditMenu(Region region){
        setTitle("&eEditing " + region.getName());

        setSize(9 * 6);

        rename = new Button() {
            @Override
            public void onClickedInMenu(Player player, Menu menu, ClickType clickType) {
                player.sendMessage(CC.color("&eType the new name in chat!"));

                player.getOpenInventory().close();

                ChatListener.renameMode.put(player.getUniqueId(), region);
            }

            @Override
            public ItemStack getItem() {
                return ItemBuilder.from(Material.NAME_TAG)
                        .name("&eRename")
                        .lore(
                                "",
                                "&7Click to rename the region"
                        )
                        .build();
            }
        };

        addPlayer = new Button() {
            @Override
            public void onClickedInMenu(Player player, Menu menu, ClickType clickType) {
                player.sendMessage(CC.color("&eType the player's name in chat!"));

                player.getOpenInventory().close();

                ChatListener.addPlayerMode.put(player.getUniqueId(), region);
            }

            @Override
            public ItemStack getItem() {
                return ItemBuilder.from(Material.EMERALD)
                        .name("&aAdd Player")
                        .lore(
                                "",
                                "&7Click to add a player to the region's whitelist"
                        )
                        .build();
            }
        };

        removePlayer = new Button() {
            @Override
            public void onClickedInMenu(Player player, Menu menu, ClickType clickType) {
                player.sendMessage(CC.color("&eType the player's name in chat!"));

                player.getOpenInventory().close();

                ChatListener.removePlayerMode.put(player.getUniqueId(), region);
            }

            @Override
            public ItemStack getItem() {
                return ItemBuilder.from(Material.REDSTONE)
                        .name("&cRemove Player")
                        .lore(
                                "",
                                "&7Click to remove a player from the region's whitelist"
                        )
                        .build();
            }
        };

        redefineLocation = new Button() {
            @Override
            public void onClickedInMenu(Player player, Menu menu, ClickType clickType) {
                player.sendMessage(CC.color("&eRight click to redefine the region's location!"));

                player.getOpenInventory().close();

                WandListener.toggleEditMode(player, region);
            }

            @Override
            public ItemStack getItem() {
                return ItemBuilder.from(Material.GRASS_BLOCK)
                        .name("&aRedefine Location")
                        .lore(
                                "",
                                "&7Click to redefine the region's location"
                        )
                        .build();
            }
        };

        editFlags = new Button() {
            @Override
            public void onClickedInMenu(Player player, Menu menu, ClickType clickType) {
                new FlagsMenu(region).displayTo(player);
            }

            @Override
            public ItemStack getItem() {
                return ItemBuilder.from(Material.WHITE_BANNER)
                        .name("&fEdit Flags")
                        .lore(
                                "",
                                "&7Click to edit the region's flags"
                        )
                        .build();
            }
        };
    }
}
