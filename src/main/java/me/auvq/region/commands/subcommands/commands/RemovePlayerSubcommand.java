package me.auvq.region.commands.subcommands.commands;

import me.auvq.region.commands.subcommands.SubCommand;
import me.auvq.region.region.Region;
import me.auvq.region.region.RegionsManager;
import me.auvq.region.utils.CC;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class RemovePlayerSubcommand implements SubCommand {
    @Override
    public boolean onCommand(CommandSender sender, String[] args) {
        Player player = Bukkit.getPlayer(args[1]);
        if(args.length != 2) {
            sender.sendMessage(CC.color("&cUsage: /region removeplayer <region> <player>"));
            return true;
        }

        if(!sender.hasPermission("region.remove") || !sender.hasPermission("region.bypass")){
            sender.sendMessage(CC.color("&cYou do not have permission to use this command!"));
            return true;
        }

        if (player == null) {
            sender.sendMessage(CC.color("&cPlayer not found! Make sure the player is online!"));
            return true;
        }
        Region region = plugin.getRegionsManager().getRegion(args[0]);
        if (region == null) {
            sender.sendMessage(CC.color("&cRegion not found!"));
            return true;
        }
        if (!region.getAllowedPlayers().contains(player.getUniqueId())) {
            sender.sendMessage(CC.color("&cPlayer not found in the region's whitelist!"));
            return true;
        }

        region.removePlayer(player.getUniqueId());
        sender.sendMessage(CC.color("&eSuccessfully removed &6" + player.getName() + " &efrom the region's whitelist"));
        return true;
    }
}
