package me.auvq.region.commands.subcommands;

import org.bukkit.command.CommandSender;

public interface SubCommand {
    boolean onCommand(CommandSender sender, String[] args);
}
