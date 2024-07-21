package me.auvq.region.menu;


import io.lumine.mythic.core.menus.MainMenu;
import me.auvq.region.region.Region;
import me.auvq.region.region.RegionsManager;
import me.auvq.region.utils.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.mineacademy.fo.menu.Menu;
import org.mineacademy.fo.menu.MenuPagged;
import org.mineacademy.fo.menu.RegionMenu;
import org.mineacademy.fo.menu.button.Button;
import org.mineacademy.fo.menu.button.annotation.Position;

import java.awt.*;

public class RegionsMenu extends MenuPagged<Region> {

    public RegionsMenu(){
        super(RegionsManager.getRegions());

        this.setSize(9 * 6);
        this.setTitle("&eRegions Menu");
    }

    @Override
    protected ItemStack convertToItemStack(Region region) {
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

    @Override
    protected void onPageClick(Player player, Region region, ClickType clickType) {
        new RegionEditMenu(region).displayTo(player);
    }
}
