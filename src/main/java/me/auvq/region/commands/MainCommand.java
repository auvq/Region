package me.auvq.region.commands;

import com.comphenix.protocol.PacketType;
import dev.rollczi.litecommands.annotations.argument.Arg;
import dev.rollczi.litecommands.annotations.command.Command;
import dev.rollczi.litecommands.annotations.context.Context;
import dev.rollczi.litecommands.annotations.execute.Execute;
import dev.rollczi.litecommands.annotations.permission.Permission;
import me.auvq.region.flag.Flag;
import me.auvq.region.flag.FlagManager;
import me.auvq.region.listeners.WandListener;
import me.auvq.region.menu.RegionEditMenu;
import me.auvq.region.menu.RegionsMenu;
import me.auvq.region.region.Region;
import me.auvq.region.region.RegionsManager;
import me.auvq.region.utils.CC;
import me.auvq.region.utils.spider.SpiderCuboid;
import me.auvq.region.utils.spider.SpiderLocation;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Optional;

@Command(name = "region", aliases = "rg")
public class MainCommand {

    @Execute
    @Permission("region.menu")
    public void openGUI(@Context CommandSender sender) {
        new RegionsMenu().displayTo((Player) sender);
    }

    @Execute(name = "wand")
    @Permission("region.wand")
    public void editMode(@Context CommandSender sender) {
        Player player = (Player) sender;
        WandListener.toggleEditMode(player);
    }

    @Execute(name = "create")
    @Permission("region.create")
    public void onCreate(@Context CommandSender sender, @Arg String regionName) {
        Player player = (Player) sender;
        Location firstLocation = player.getLocation().add(5, 0, 5);
        Location secondLocation = player.getLocation().subtract(5, 0, 5);
        SpiderCuboid cuboid = new SpiderCuboid(SpiderLocation.from(firstLocation), SpiderLocation.from(secondLocation));

        RegionsManager.addRegion(new Region(regionName, cuboid));

        player.sendMessage(CC.color("&eCreated a new region called &6" + regionName));
    }

    @Execute(name = "add")
    @Permission("region.add")
    public void addPlayer(@Context CommandSender sender, @Arg String regionName, @Arg String playerName){
        Player player = Bukkit.getPlayer(playerName);

        if(player == null) {
            sender.sendMessage(CC.color("&cPlayer not found! Make sure the player is online!"));
            return;
        }
        if(RegionsManager.getRegion(regionName) == null) {
            sender.sendMessage(CC.color("&cRegion not found!"));
            return;
        }
        if(RegionsManager.getRegion(regionName).getAllowedPlayers().contains(player.getUniqueId())) {
            sender.sendMessage(CC.color("&cPlayer already found in the region's whitelist!"));
            return;
        }

        RegionsManager.getRegion(regionName).addPlayer(player.getUniqueId());
        sender.sendMessage(CC.color("&eSuccessfully added &6" + player.getName() + " &eto the region's whitelist"));
    }

    @Execute(name = "remove")
    @Permission("region.remove")
    public void removePlayer(@Context CommandSender sender, @Arg String regionName, @Arg String playerName){
        Player player = Bukkit.getPlayer(playerName);

        if(player == null) {
            sender.sendMessage(CC.color("&cPlayer not found! Make sure the player is online!"));
            return;
        }
        if(RegionsManager.getRegion(regionName) == null) {
            sender.sendMessage(CC.color("&cRegion not found!"));
            return;
        }

        if(!RegionsManager.getRegion(regionName).getAllowedPlayers().contains(player.getUniqueId())) {
            sender.sendMessage(CC.color("&cPlayer not found in the region's whitelist!"));
            return;
        }

        RegionsManager.getRegion(regionName).removePlayer(player.getUniqueId());
        sender.sendMessage(CC.color("&eSuccessfully removed &6" + player.getName() + " &efrom the region's whitelist"));
    }

    @Execute(name = "whitelist")
    @Permission("region.whitelist")
    public void sendWhitelist(@Context CommandSender sender, @Arg String regionName){
        if(RegionsManager.getRegion(regionName) == null) {
            sender.sendMessage(CC.color("&cRegion not found!"));
            return;
        }

        sender.sendMessage(CC.color("&eWhitelist for region &6" + regionName + "&e:"));
        RegionsManager.getRegion(regionName).getAllowedPlayers().forEach(uuid -> {
            sender.sendMessage(CC.color("&6- " + Bukkit.getOfflinePlayer(uuid).getName()));
        });
    }

    @Execute
    @Permission("region.menu")
    public void openRegionGUI(@Context CommandSender sender, @Arg String regionName){
        Region region = RegionsManager.getRegion(regionName);
        if(region == null) {
            sender.sendMessage(CC.color("&cRegion not found!"));
            return;
        }

        new RegionEditMenu(region).displayTo((Player) sender);
    }

    @Execute(name = "flag")
    @Permission("region.flag")
    public void editFlag(@Context CommandSender sender, @Arg String regionName, @Arg FlagManager.FlagType flagType, @Arg Flag.State state){
        Region region = RegionsManager.getRegion(regionName);
        if(region == null) {
            sender.sendMessage(CC.color("&cRegion not found!"));
            return;
        }
        Optional<Flag> flag = region.getFlags().stream().filter(f -> f.getType() == flagType).findFirst();
        if(!flag.isPresent()) {
            sender.sendMessage(CC.color("&cFlag not found!"));
            return;
        }

        flag.get().setState(state);

        sender.sendMessage(CC.color("&eSet flag &6" + flagType.name().toUpperCase() + " &eto &6" + state.name().toUpperCase()));
    }
}
