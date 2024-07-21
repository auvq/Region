package me.auvq.region.commands.args;

import dev.rollczi.litecommands.argument.Argument;
import dev.rollczi.litecommands.argument.parser.ParseResult;
import dev.rollczi.litecommands.argument.resolver.ArgumentResolver;
import dev.rollczi.litecommands.invocation.Invocation;
import dev.rollczi.litecommands.suggestion.SuggestionContext;
import dev.rollczi.litecommands.suggestion.SuggestionResult;
import me.auvq.region.Main;
import me.auvq.region.flag.Flag;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class FlagState extends ArgumentResolver<CommandSender, Flag.State> {

    private static final Map<String, Flag.State> STATE_ARGUMENTS = new HashMap<>();

    static {
        Flag.State[] states = Flag.State.values();
        Arrays.stream(states).forEach(state -> STATE_ARGUMENTS.put(state.name(), state));
    }

    @Override
    protected ParseResult<Flag.State> parse(Invocation<CommandSender> invocation, Argument<Flag.State> context, String argument) {
        Flag.State state = STATE_ARGUMENTS.get(argument.toUpperCase());

        if (state == null) {
            return ParseResult.failure("Invalid food argument!");
        }

        return ParseResult.success(state);
    }

    @Override
    public SuggestionResult suggest(Invocation<CommandSender> invocation, Argument<Flag.State> argument, SuggestionContext context) {
        return SuggestionResult.of(STATE_ARGUMENTS.keySet());
    }
}
