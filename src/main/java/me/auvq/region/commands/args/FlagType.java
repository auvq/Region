package me.auvq.region.commands.args;

import dev.rollczi.litecommands.argument.Argument;
import dev.rollczi.litecommands.argument.parser.ParseResult;
import dev.rollczi.litecommands.argument.resolver.ArgumentResolver;
import dev.rollczi.litecommands.invocation.Invocation;
import dev.rollczi.litecommands.suggestion.SuggestionContext;
import dev.rollczi.litecommands.suggestion.SuggestionResult;
import me.auvq.region.Main;
import me.auvq.region.flag.Flag;
import me.auvq.region.flag.FlagManager;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class FlagType extends ArgumentResolver<CommandSender, FlagManager.FlagType> {

    private static final Map<String, FlagManager.FlagType> FLAG_TYPE_ARGUMENTS = new HashMap<>();

    static {
        FlagManager.FlagType[] flagTypes = FlagManager.FlagType.values();
        Arrays.stream(flagTypes).forEach(flag -> FLAG_TYPE_ARGUMENTS.put(flag.name(), flag));
    }

    @Override
    protected ParseResult<FlagManager.FlagType> parse(Invocation<CommandSender> invocation, Argument<FlagManager.FlagType> context, String argument) {
        FlagManager.FlagType flagType = FLAG_TYPE_ARGUMENTS.get(argument.toUpperCase());

        if (flagType == null) {
            return ParseResult.failure("Invalid food argument!");
        }

        return ParseResult.success(flagType);
    }

    @Override
    public SuggestionResult suggest(Invocation<CommandSender> invocation, Argument<FlagManager.FlagType> argument, SuggestionContext context) {
        return SuggestionResult.of(FLAG_TYPE_ARGUMENTS.keySet());
    }
}
