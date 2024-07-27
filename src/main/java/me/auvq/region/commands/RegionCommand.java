package me.auvq.region.commands;

import me.auvq.region.commands.subcommands.commands.*;
import me.auvq.region.commands.subcommands.SubCommand;
import me.auvq.region.menu.RegionsMenu;
import me.auvq.region.utils.CC;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class RegionCommand implements CommandExecutor {
    private final Map<String, SubCommand> subCommands = new HashMap<>();

    public RegionCommand() {
        // Initialize subcommands
        subCommands.put("wand", new WandSubcommand());
        subCommands.put("create", new CreateSubcommand());
        subCommands.put("add", new AddPlayerSubcommand());
        subCommands.put("remove", new RemovePlayerSubcommand());
        subCommands.put("whitelist", new WhitelistSubcommand());
        subCommands.put("menu", new OpenRegionGUISubcommand());
        subCommands.put("flag", new EditFlagSubcommand());
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(!(sender instanceof Player)) {
            sender.sendMessage(CC.color("&cOnly players can use this command."));
            return true;
        }

        if (args.length == 0) {
            if(!sender.hasPermission("region.menu")) {
                sender.sendMessage(CC.color("&cYou do not have permission to use this command."));
                return true;
            }
            new RegionsMenu().show((Player) sender);
            return true;
        }

        if(args.length == 1 && Objects.equals(args[0], "help")){
            sender.sendMessage(CC.color("&eRegion plugin - created by auvq:"));
            sender.sendMessage(CC.color("&7&m-----------------------------------"));
            sender.sendMessage(CC.color("&e- /region - Opens the region menu"));
            sender.sendMessage(CC.color("&e- /region help - Displays this message"));
            sender.sendMessage(CC.color("&e- /region wand - Toggles edit mode"));
            sender.sendMessage(CC.color("&e- /region create <name> - Creates a new region"));
            sender.sendMessage(CC.color("&e- /region add <region> <player> - Adds a player to the region's whitelist"));
            sender.sendMessage(CC.color("&e- /region remove <region> <player> - Removes a player from the region's whitelist"));
            sender.sendMessage(CC.color("&e- /region whitelist <region> - Lists all players in the region's whitelist"));
            sender.sendMessage(CC.color("&e- /region flag <region> <flag> <value> - Edits a flag for the region"));
            sender.sendMessage(CC.color("&e- /region <region> - Opens a menu for the region"));
            sender.sendMessage(CC.color("&7&m-----------------------------------"));
            return true;
        }

        SubCommand subCommand = subCommands.get(args[0].toLowerCase());
        if (subCommand != null) {
            return subCommand.onCommand(sender, Arrays.copyOfRange(args, 1, args.length));
        } else {
            sender.sendMessage("Unknown command. Use /region help for a list of commands.");
            return true;
        }
    }
}