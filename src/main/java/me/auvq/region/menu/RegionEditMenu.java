package me.auvq.region.menu;

import com.github.stefvanschie.inventoryframework.gui.GuiItem;
import com.github.stefvanschie.inventoryframework.gui.type.ChestGui;
import com.github.stefvanschie.inventoryframework.pane.PatternPane;
import com.github.stefvanschie.inventoryframework.pane.util.Pattern;
import me.auvq.region.listeners.ChatListener;
import me.auvq.region.listeners.WandListener;
import me.auvq.region.region.Region;
import me.auvq.region.utils.CC;
import me.auvq.region.utils.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

public class RegionEditMenu extends ChestGui {


    private Region region;

    public RegionEditMenu(Region region) {
        super(6, CC.color("&eEditing " + region.getName()));

        this.region = region;
        this.setOnGlobalClick(event -> event.setCancelled(true));
        fillItems();
    }

    private void fillItems() {
        final Pattern pattern = new Pattern(
                "000000000",
                "000000000",
                "010203040",
                "000000000",
                "000050000",
                "000000000"
        );

        final PatternPane patternPane = new PatternPane(
                9,
                6, pattern
        );

        patternPane.bindItem('0', new GuiItem(
                        new ItemStack(Material.BLACK_STAINED_GLASS_PANE),
                        event -> event.setCancelled(true)
                )
        );

        patternPane.bindItem('1', new GuiItem(
                        ItemBuilder.from(Material.NAME_TAG)
                                .name("&eRename")
                                .lore(
                                        "",
                                        "&7Click to rename the region"
                                )
                                .build(),
                        event -> {
                            event.getWhoClicked().sendMessage(CC.color("&eType the new name in chat!"));

                            event.getWhoClicked().getOpenInventory().close();

                            ChatListener.renameMode.put(event.getWhoClicked().getUniqueId(), region);
                        })
                );

        patternPane.bindItem('2', new GuiItem(
                        ItemBuilder.from(Material.EMERALD)
                                .name("&aAdd Player")
                                .lore(
                                        "",
                                        "&7Click to add a player to the region's whitelist"
                                )
                                .build(),
                        event -> {
                            event.getWhoClicked().sendMessage(CC.color("&eType the player's name in chat!"));

                            event.getWhoClicked().getOpenInventory().close();

                            ChatListener.addPlayerMode.put(event.getWhoClicked().getUniqueId(), region);
                        })
                );

        patternPane.bindItem('3', new GuiItem(
                        ItemBuilder.from(Material.REDSTONE)
                                .name("&cRemove Player")
                                .lore(
                                        "",
                                        "&7Click to remove a player from the region's whitelist"
                                )
                                .build(),
                        event -> {
                            event.getWhoClicked().sendMessage(CC.color("&eType the player's name in chat!"));

                            event.getWhoClicked().getOpenInventory().close();

                            ChatListener.removePlayerMode.put(event.getWhoClicked().getUniqueId(), region);
                        })
                );

        patternPane.bindItem('4', new GuiItem(
                        ItemBuilder.from(Material.GRASS_BLOCK)
                                .name("&aRedefine Location")
                                .lore(
                                        "",
                                        "&7Click to redefine the region's location"
                                )
                                .build(),
                        event -> {
                            event.getWhoClicked().sendMessage(CC.color("&eRight click to redefine the region's location!"));

                            event.getWhoClicked().getOpenInventory().close();

                            WandListener.toggleEditMode((Player) event.getWhoClicked(), region);
                        })
                );

        patternPane.bindItem('5', new GuiItem(
                ItemBuilder.from(Material.WHITE_BANNER)
                        .name("&fEdit Flags")
                        .lore(
                                "",
                                "&7Click to edit the region's flags"
                        )
                        .build(),
                event -> {
                    new FlagsMenu(region).show(event.getWhoClicked());
                })
        );


        this.addPane(patternPane);
    }
}
