package me.auvq.region.menu;


import com.github.stefvanschie.inventoryframework.gui.GuiItem;
import com.github.stefvanschie.inventoryframework.gui.type.ChestGui;
import com.github.stefvanschie.inventoryframework.pane.PatternPane;
import com.github.stefvanschie.inventoryframework.pane.StaticPane;
import com.github.stefvanschie.inventoryframework.pane.util.Pattern;
import me.auvq.region.Main;
import me.auvq.region.region.Region;
import me.auvq.region.region.RegionsManager;
import me.auvq.region.utils.CC;
import me.auvq.region.utils.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.awt.*;
import java.util.List;

public class RegionsMenu extends ChestGui {

    private final Main plugin = Main.getInstance();

    public RegionsMenu(){
        super(6, CC.color("&eRegions Menu"));

        this.setOnGlobalClick(event -> event.setCancelled(true));

        fillItems();
    }

    private ItemStack convertToItemStack(Region region) {
        return ItemBuilder.from(Material.GRASS_BLOCK)
                .name("&6" + region.getName())
                .lore(
                        "",
                        "&7Region Location: ",
                        "&7- &eWorld: &f" + region.getCuboid().getCenter().getWorld(),
                        "&7- &eX: &f" + region.getCuboid().getCenter().getX(),
                        "&7- &eY: &f" + region.getCuboid().getCenter().getY(),
                        "&7- &eZ: &f" + region.getCuboid().getCenter().getZ(),
                        "",
                        "&7Left click to edit region"
                )
                .build();
    }

    private void onPageClick(Player player, Region region) {
        new RegionEditMenu(region).show(player);
    }

    private void fillItems() {
        final Pattern glassPattern = new Pattern(
                "000000000",
                "011111110",
                "011111110",
                "011111110",
                "011111110",
                "000000000"
        );

        final PatternPane glassPane = new PatternPane(
                9,
                6,
                glassPattern
        );

        glassPane.bindItem('0', new GuiItem(
                new ItemStack(Material.BLACK_STAINED_GLASS_PANE),
                event -> event.setCancelled(true)
        ));

        StaticPane regionPane = new StaticPane(0, 0, 9, 6);

        List<Region> regions = plugin.getRegionsManager().getRegions();
        int regionIndex = 0;

        for (int y = 0; y < glassPattern.getHeight(); y++) {
            for (int x = 0; x < glassPattern.getLength(); x++) {
                if (glassPattern.getCharacter(x, y) == '1' && regionIndex < regions.size()) {
                    Region region = regions.get(regionIndex++);
                    regionPane.addItem(new GuiItem(
                            convertToItemStack(region),
                            event -> onPageClick((Player) event.getWhoClicked(), region)
                    ), x, y);
                }
            }
        }

        addPane(glassPane);
        addPane(regionPane);
    }
}
