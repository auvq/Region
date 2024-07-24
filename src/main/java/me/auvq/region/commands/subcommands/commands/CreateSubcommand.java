package me.auvq.region.commands.subcommands.commands;

import me.auvq.region.commands.subcommands.SubCommand;
import me.auvq.region.region.Region;
import me.auvq.region.region.RegionsManager;
import me.auvq.region.utils.CC;
import me.auvq.region.utils.spider.SpiderCuboid;
import me.auvq.region.utils.spider.SpiderLocation;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CreateSubcommand implements SubCommand {
    @Override
    public boolean onCommand(CommandSender sender, String[] args) {
        Player player = (Player) sender;
        if(args.length != 1) {
            player.sendMessage(CC.color("&cUsage: /region create <name>"));
            return true;
        }
        if(!player.hasPermission("region.create") || !sender.hasPermission("region.bypass")) {
            player.sendMessage(CC.color("&cYou do not have permission to use this command!"));
            return true;
        }

        Location firstLocation = player.getLocation().add(5, 0, 5);
        Location secondLocation = player.getLocation().subtract(5, 0, 5);
        SpiderCuboid cuboid = new SpiderCuboid(SpiderLocation.from(firstLocation), SpiderLocation.from(secondLocation));

        plugin.getRegionsManager().addRegion(new Region(args[0], cuboid));

        player.sendMessage(CC.color("&eCreated a new region called &6" + args[0]));
        return true;
    }
}
