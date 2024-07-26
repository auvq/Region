package me.auvq.region.commands.subcommands.commands;

import me.auvq.region.commands.subcommands.SubCommand;
import me.auvq.region.listeners.WandListener;
import me.auvq.region.utils.CC;
import net.kyori.adventure.text.Component;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class WandSubcommand implements SubCommand {
    @Override
    public boolean onCommand(CommandSender sender, String[] args) {

        if(!sender.hasPermission("region.wand") || !sender.hasPermission("region.bypass")){
            sender.sendMessage(CC.color("&cYou do not have permission to use this command!"));
            return true;
        }

        Player player = (Player) sender;

        plugin.getWandListener().toggleWand(player);
        return true;
    }
}
