package me.auvq.region.menu;

import me.auvq.region.flag.Flag;
import me.auvq.region.region.Region;
import me.auvq.region.utils.CC;
import me.auvq.region.utils.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.mineacademy.fo.menu.MenuPagged;

public class FlagsMenu extends MenuPagged<Flag> {

    private Region region;

    public FlagsMenu(Region region){
        super(region.getFlags());

        this.region = region;
    }

    @Override
    protected ItemStack convertToItemStack(Flag flag) {
        return ItemBuilder.from(Material.WHITE_BANNER)
                .name("&6" + flag.getType().name())
                .lore(
                        "",
                        "&7Flag Description: ",
                        "&7- &e" + flag.getDescription(),
                        "",
                        "&7Flag State: ",
                        "&7- &e" + flag.getState().name(),
                        "",
                        "&7Left click to switch state!"
                )
                .build();
    }

    @Override
    protected void onPageClick(Player player, Flag flag, ClickType clickType) {
        flag.setState(flag.getState() == Flag.State.EVERYONE ? Flag.State.WHITELIST : flag.getState() == Flag.State.WHITELIST ? Flag.State.NONE : Flag.State.EVERYONE);
        player.sendMessage(CC.color("&eFlag state changed to &6" + flag.getState().name()));
        restartMenu();
        region.updateData();
    }
}
