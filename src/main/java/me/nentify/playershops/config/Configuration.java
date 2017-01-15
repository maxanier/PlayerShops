package me.nentify.playershops.config;

import ninja.leaping.configurate.Types;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.hocon.HoconConfigurationLoader;
import ninja.leaping.configurate.loader.ConfigurationLoader;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class Configuration {

    public static double creationCost;
    public static double tax;
    public static List<String> limitToWorlds;
    public static boolean chestBelow;
    public static boolean chestBehind;
    private ConfigurationLoader<CommentedConfigurationNode> loader;
    private CommentedConfigurationNode config;

    public Configuration(Path configPath) throws IOException {
        loader = HoconConfigurationLoader.builder().setPath(configPath).build();

        if (!Files.exists(configPath))
            Files.createFile(configPath);

        config = loader.load();

        creationCost = check(config.getNode("creation-cost"), 0.0, "Cost to create a shop").getDouble();
        tax = check(config.getNode("tax"), 0.0, "Tax on transactions made with shops (decimal between 0 and 1)").getDouble();
        limitToWorlds = check(config.getNode("limit-to-worlds"), new ArrayList<String>(), "Limit shop creation to certain worlds - Shops can be created in all worlds if left empty").getList(Types::asString);
        chestBelow = check(config.getNode("chest-below"), true, "If the shop sign sign can be placed above the chest").getBoolean();
        chestBehind = check(config.getNode("chest-behind"), true, "If the shop sign sign can be placed in front of the chest").getBoolean();

        loader.save(config);
    }

    private CommentedConfigurationNode check(CommentedConfigurationNode node, Object defaultValue, String comment) {
        if (node.isVirtual())
            node.setValue(defaultValue).setComment(comment);

        return node;
    }
}
