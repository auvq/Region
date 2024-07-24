package me.auvq.region.commands.subcommands.commands;

import me.auvq.region.commands.subcommands.SubCommand;
import me.auvq.region.region.Region;
import me.auvq.region.region.RegionsManager;
import me.auvq.region.utils.CC;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

public class WhitelistSubcommand implements SubCommand {
    @Override
    public boolean onCommand(CommandSender sender, String[] args) {

        if(args.length != 1) {
            sender.sendMessage(CC.color("&cUsage: /region whitelist <region>"));
            return true;
        }

        if(!sender.hasPermission("region.whitelist") || !sender.hasPermission("region.bypass")) {
            sender.sendMessage(CC.color("&cYou do not have permission to use this command!"));
            return true;
        }

        Region region = plugin.getRegionsManager().getRegion(args[0]);
        if (region == null) {
            sender.sendMessage(CC.color("&cRegion not found!"));
            return true;
        }

        StringBuilder whitelist = new StringBuilder("&eWhitelist for region &6" + args[0] + "&e:");
        region.getAllowedPlayers().forEach(uuid -> whitelist.append("\n- ").append(Bukkit.getOfflinePlayer(uuid).getName()));
        sender.sendMessage(CC.color(whitelist.toString()));
        return true;
    }
}