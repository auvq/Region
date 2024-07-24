package me.auvq.region.menu;

import com.github.stefvanschie.inventoryframework.gui.GuiItem;
import com.github.stefvanschie.inventoryframework.gui.InventoryComponent;
import com.github.stefvanschie.inventoryframework.gui.type.ChestGui;
import com.github.stefvanschie.inventoryframework.gui.type.util.Gui;
import com.github.stefvanschie.inventoryframework.pane.Pane;
import com.github.stefvanschie.inventoryframework.pane.PatternPane;
import com.github.stefvanschie.inventoryframework.pane.StaticPane;
import com.github.stefvanschie.inventoryframework.pane.util.Pattern;
import me.auvq.region.Main;
import me.auvq.region.flag.Flag;
import me.auvq.region.region.Region;
import me.auvq.region.utils.CC;
import me.auvq.region.utils.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.List;

public class FlagsMenu extends ChestGui {

    private final Region region;

    public FlagsMenu(Region region){
        super(6, CC.color("&eEditing &6" + region.getName() + "&e's flags"));

        this.region = region;

        this.setOnGlobalClick(event -> event.setCancelled(true));

        fillItems();
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

        StaticPane flagPane = new StaticPane(0, 0, 9, 6);

        List<Flag> flags = region.getFlags();

        int flagIndex = 0;

        for (int y = 0; y < glassPattern.getHeight(); y++) {
            for (int x = 0; x < glassPattern.getLength(); x++) {
                if (glassPattern.getCharacter(x, y) == '1' && flagIndex < flags.size()) {
                    Flag flag = flags.get(flagIndex++);
                    flagPane.addItem(new GuiItem(
                            convertToItemStack(flag),
                            event -> onPageClick((Player) event.getWhoClicked(), flag, event.getClick())
                    ), x, y);
                }
            }
        }

        addPane(glassPane);
        addPane(flagPane);
    }

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


    protected void onPageClick(Player player, Flag flag, ClickType clickType) {
        flag.setState(flag.getState() == Flag.State.EVERYONE ? Flag.State.WHITELIST : flag.getState() == Flag.State.WHITELIST ? Flag.State.NONE : Flag.State.EVERYONE);
        player.sendMessage(CC.color("&eFlag state changed to &6" + flag.getState().name()));

        this.update();

        region.updateData();
    }
}
