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
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

@Getter
public final class Main extends JavaPlugin {

    @Getter
    private static Main instance;

    private MongoClient mongoClient;

    private MongoCollection<Document> collection;

    private FlagManager flagManager;

    private RegionsManager regionsManager;

    WandListener wandListener;

    ChatListener chatListener;

    @Override
    public void onEnable() {
        instance = this;

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

        getServer().getPluginManager().registerEvents(wandListener = new WandListener(), this);
        getServer().getPluginManager().registerEvents(chatListener = new ChatListener(), this);

        getServer().getConsoleSender().sendMessage(CC.color("&aMongoDB successfully setup!"));

        flagManager = new FlagManager();
        regionsManager = new RegionsManager();

        addFlags();

        regionsManager.getRegions().forEach(region -> {
            region.getFlags().forEach(flag -> getServer().getPluginManager().registerEvents(flag, this));
        });

        this.getCommand("regions").setExecutor(new RegionCommand());
        this.getCommand("regions").setTabCompleter(new RegionTabCompleter());
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public void addFlags() {
        flagManager.registerFlagCreator(BlockBreakFlag.type, () -> new BlockBreakFlag(Flag.State.WHITELIST));
        flagManager.registerFlagCreator(BlockPlaceFlag.type, () -> new BlockPlaceFlag(Flag.State.WHITELIST));
        flagManager.registerFlagCreator(EntityDamageFlag.type, () -> new EntityDamageFlag(Flag.State.WHITELIST));
        flagManager.registerFlagCreator(InteractFlag.type, () -> new InteractFlag(Flag.State.WHITELIST));
    }
}