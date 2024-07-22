package me.auvq.region.commands;

import me.auvq.region.region.RegionsManager;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class RegionTabCompleter implements TabCompleter {

    private static final List<String> SUBCOMMANDS = Arrays.asList("create", "add", "remove", "whitelist", "flag", "help");
    private static final List<String> FLAGS = Arrays.asList("block-break", "block-place", "entity-damage", "interact"); // Example flag names
    private static final List<String> FLAG_VALUES = Arrays.asList("allow", "deny", "whitelist"); // Example flag values

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> completions = new ArrayList<>();

        if (args.length == 1) {
            StringUtil.copyPartialMatches(args[0], SUBCOMMANDS, completions);
        } else if (args.length == 2) {
            switch (args[0].toLowerCase()) {
                case "add":
                case "remove":
                case "whitelist":
                    List<String> regionNames = RegionsManager.getRegions().stream()
                            .map(region -> region.getName())
                            .collect(Collectors.toList());
                    StringUtil.copyPartialMatches(args[1], regionNames, completions);
                    break;
                case "flag":
                    StringUtil.copyPartialMatches(args[1], FLAGS, completions);
                    break;
            }
        } else if (args.length == 3 && "flag".equals(args[0].toLowerCase())) {
            StringUtil.copyPartialMatches(args[2], FLAG_VALUES, completions);
        } else if (args.length == 3 && ("add".equals(args[0].toLowerCase()) || "remove".equals(args[0].toLowerCase()) || "whitelist".equals(args[0].toLowerCase()))) {
            List<String> playerNames = Bukkit.getOnlinePlayers().stream()
                    .map(Player::getName)
                    .collect(Collectors.toList());
            StringUtil.copyPartialMatches(args[2], playerNames, completions);
        }

        return completions;
    }
}