package me.auvq.region;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import lombok.Getter;
import me.auvq.region.commands.RegionCommand;
import me.auvq.region.commands.RegionTabCompleter;
import me.auvq.region.flag.Flag;
import me.auvq.region.flag.FlagManager;
import me.auvq.region.flag.flags.BlockBreakFlag;
import me.auvq.region.flag.flags.BlockPlaceFlag;
import me.auvq.region.flag.flags.EntityDamageFlag;
import me.auvq.region.flag.flags.InteractFlag;
import me.auvq.region.listeners.ChatListener;
import me.auvq.region.listeners.WandListener;
import me.auvq.region.region.RegionsManager;
import me.auvq.region.utils.CC;
import org.bson.Document;
import org.bukkit.command.CommandSender;
import org.mineacademy.fo.plugin.SimplePlugin;

import java.util.Objects;

@Getter
public final class Main extends SimplePlugin {

    @Getter
    private static Main instance;

    private MongoClient mongoClient;

    private MongoCollection<Document> collection;

    @Override
    public void onPluginStart() {
        instance = (Main) SimplePlugin.getInstance();

        try {
            mongoClient = MongoClients.create(Objects.requireNonNull(getConfig().getString("mongo.uri")));

            collection = mongoClient
                    .getDatabase(Objects.requireNonNull(getConfig().getString("mongo.hostname")))
                    .getCollection(Objects.requireNonNull(getConfig().getString("mongo.collection")));
        } catch (Exception e) {
            getServer().getConsoleSender().sendMessage(CC.color("&cFailed to setup MongoDB!"));
            e.printStackTrace();
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        getServer().getPluginManager().registerEvents(new WandListener(), this);
        getServer().getPluginManager().registerEvents(new ChatListener(), this);

        this.getCommand("regions").setExecutor(new RegionCommand());
        this.getCommand("region").setTabCompleter(new RegionTabCompleter());

        getServer().getConsoleSender().sendMessage(CC.color("&aMongoDB successfully setup!"));

        addFlags();

        RegionsManager.loadRegions();

        RegionsManager.getRegions().forEach(region -> {
            region.getFlags().forEach(flag -> getServer().getPluginManager().registerEvents(flag, this));
        });
    }

    @Override
    public void onPluginStop() {
        // Plugin shutdown logic
    }

    public void addFlags() {
        FlagManager.registerFlagCreator(BlockBreakFlag.type, () -> new BlockBreakFlag(Flag.State.WHITELIST));
        FlagManager.registerFlagCreator(BlockPlaceFlag.type, () -> new BlockPlaceFlag(Flag.State.WHITELIST));
        FlagManager.registerFlagCreator(EntityDamageFlag.type, () -> new EntityDamageFlag(Flag.State.WHITELIST));
        FlagManager.registerFlagCreator(InteractFlag.type, () -> new InteractFlag(Flag.State.WHITELIST));
    }
}