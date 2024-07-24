package me.auvq.region.commands.subcommands;

import me.auvq.region.Main;
import org.bukkit.command.CommandSender;

public interface SubCommand {

    Main plugin = Main.getInstance();

    boolean onCommand(CommandSender sender, String[] args);
}
