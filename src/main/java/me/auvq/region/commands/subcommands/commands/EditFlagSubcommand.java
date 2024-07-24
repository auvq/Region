package me.auvq.region.commands.subcommands.commands;

import me.auvq.region.commands.subcommands.SubCommand;
import me.auvq.region.flag.Flag;
import me.auvq.region.flag.FlagManager;
import me.auvq.region.region.Region;
import me.auvq.region.region.RegionsManager;
import me.auvq.region.utils.CC;
import org.bukkit.command.CommandSender;

import java.util.Optional;

public class EditFlagSubcommand implements SubCommand {
    @Override
    public boolean onCommand(CommandSender sender, String[] args) {
        if (args.length < 3) {
            sender.sendMessage(CC.color("&cUsage: /region flag <region> <flag> <value>"));
            return true;
        }
        if(!sender.hasPermission("region.flag") || !sender.hasPermission("region.bypass")) {
            sender.sendMessage(CC.color("&cYou do not have permission to use this command!"));
            return true;
        }
        Region region = plugin.getRegionsManager().getRegion(args[0]);
        if (region == null) {
            sender.sendMessage(CC.color("&cRegion not found!"));
            return true;
        }
        Optional<Flag> flag = region.getFlags().stream().filter(f -> f.getType() == FlagManager.FlagType.valueOf(args[1])).findFirst();
        if(!flag.isPresent()) {
            sender.sendMessage(CC.color("&cFlag not found!"));
            return true;
        }

        flag.get().setState(Flag.State.valueOf(args[2]));

        sender.sendMessage(CC.color("&eFlag &6" + args[1] + " &eset to &6" + args[2] + " &efor region &6" + args[0]));
        return true;
    }
}