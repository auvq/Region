package me.auvq.region.commands.subcommands.commands;

import me.auvq.region.commands.subcommands.SubCommand;
import me.auvq.region.menu.RegionEditMenu;
import me.auvq.region.region.Region;
import me.auvq.region.region.RegionsManager;
import me.auvq.region.utils.CC;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class OpenRegionGUISubcommand implements SubCommand {
    @Override
    public boolean onCommand(CommandSender sender, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Only players can use this command.");
            return true;
        }

        if(!sender.hasPermission("region.menu") || !sender.hasPermission("region.bypass")) {
            sender.sendMessage(CC.color("&cYou do not have permission to use this command!"));
            return true;
        }


        Region region = plugin.getRegionsManager().getRegion(args[0]);

        if(region == null) {
            sender.sendMessage(CC.color("&cRegion not found!"));
            return true;
        }

        new RegionEditMenu(region).show((Player) sender);
        return true;
    }
}