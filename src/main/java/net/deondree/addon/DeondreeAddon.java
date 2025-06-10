package net.deondree.addon;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import meteordevelopment.meteorclient.MeteorClient;
import meteordevelopment.meteorclient.pathing.BaritoneUtils;
import meteordevelopment.meteorclient.systems.hud.Hud;
import meteordevelopment.meteorclient.utils.Utils;
import net.deondree.addon.commands.BetterListWaypoinsCommand;
import net.deondree.addon.commands.CreditCommand;
import com.mojang.logging.LogUtils;
import meteordevelopment.meteorclient.addons.GithubRepo;
import meteordevelopment.meteorclient.addons.MeteorAddon;
import meteordevelopment.meteorclient.commands.Commands;
import meteordevelopment.meteorclient.systems.hud.HudGroup;
import meteordevelopment.meteorclient.systems.modules.Category;
import meteordevelopment.meteorclient.systems.modules.Modules;
import net.deondree.addon.commands.FriendCommand;
import net.deondree.addon.hud.FriendHUD;
import net.deondree.addon.modules.MenuModule;
import net.deondree.addon.modules.TSRBetterStashFinderModule;
import net.deondree.addon.modules.TSRChatFilterModule;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ServerInfo;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.slf4j.Logger;
import baritone.api.BaritoneAPI;
import baritone.api.pathing.goals.GoalBlock;

import java.io.File;
import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.nio.file.Files;
import java.util.List;

public class DeondreeAddon extends MeteorAddon {
    public static final Logger LOG = LogUtils.getLogger();
    public static final Category Menu = new Category("Deondree's Utils - Menu");
    public static final Category SIXBSIXT = new Category("Deondree's Utils - 6b6t.org");

    public static final HudGroup HUD_GROUP = new HudGroup("Deondree's Utils");

    public static File GetConfigFile(String key, String filename) {
        return new File(new File(new File(new File(MeteorClient.FOLDER, "Utils"), key), Utils.getFileWorldName()), filename);
    }
    public static File GetConfigFileNSS(String key, String filename) {
        File dir = new File(new File(MeteorClient.FOLDER, "Utils"), key);

        if (!dir.exists()) dir.mkdirs();

        File file = new File(dir, filename);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return file;
    }

    public boolean hasTag(String name) {
        return DeondreeAddon.GetConfigFileNSS("chat-tags", "tag-" + name + ".json").exists();
    }
    public static final List<String> VALIDCOLORS = List.of(
        "black",
        "dark_blue",
        "dark_green",
        "dark_aqua",
        "dark_red",
        "dark_purple",
        "gold",
        "gray",
        "dark_gray",
        "blue",
        "green",
        "aqua",
        "red",
        "light_purple",
        "yellow",
        "white"
    );
    //16
    public static Formatting StringToEncodeColor(String color) {
        color = color.toLowerCase();
        switch (color) {
            case "black": return Formatting.BLACK;
            case "dark_blue": return Formatting.DARK_BLUE;
            case "dark_green": return Formatting.DARK_GREEN;
            case "dark_aqua": return Formatting.DARK_AQUA;
            case "dark_red": return Formatting.DARK_RED;
            case "dark_purple": return Formatting.DARK_PURPLE;
            case "gold": return Formatting.GOLD;
            case "gray": return Formatting.GRAY;
            case "dark_gray": return Formatting.DARK_GRAY;
            case "blue": return Formatting.BLUE;
            case "green": return Formatting.GREEN;
            case "aqua": return Formatting.AQUA;
            case "red": return Formatting.RED;
            case "light_purple": return Formatting.LIGHT_PURPLE;
            case "yellow": return Formatting.YELLOW;
            case "white": return Formatting.WHITE;
            default: return Formatting.WHITE; // Fallback color
        }
    }


    public static String getColor(String name) {
        File file = DeondreeAddon.GetConfigFileNSS("chat-tags", "tag-" + name + ".json");
        if (!file.exists()) return "Gray";

        try {
            String content = Files.readString(file.toPath());
            JsonElement element = JsonParser.parseString(content);
            if (element == null || !element.isJsonObject()) return "Gray";

            JsonObject obj = element.getAsJsonObject();
            if (!obj.has("color")) return "Gray";

            JsonElement tagElement = obj.get("color");
            return tagElement.isJsonPrimitive() ? tagElement.getAsString() : "Gray";
        } catch (IOException e) {
            DeondreeAddon.LOG.error("Failed to read tag for {}: {}", name, e.getMessage());
            return "Gray";
        }
    }


    public static String getCurrentServerAddress() {
        ServerInfo server = MinecraftClient.getInstance().getCurrentServerEntry();
        if (server == null) {
            return "singleplayer";
        }

        if (server.address == null || server.address.isEmpty()) {
            return "unknown";
        }

        return MinecraftClient.getInstance().getCurrentServerEntry().address;
    }

    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    public static boolean is6B6T() {
        String serverAddress = getCurrentServerAddress();
        return serverAddress.equals("6b6t.org") || serverAddress.equals("play.6b6t.org");
    }
    public static final Text PREFIX = Text.empty()
        .append(Text.literal("[").formatted(Formatting.WHITE))
        .append(Text.literal("OmegaWare").formatted(Formatting.AQUA))
        .append(Text.literal("] ").formatted(Formatting.WHITE));

    public static boolean isBariton(){
        return BaritoneUtils.IS_AVAILABLE;
    }
    public static void GotoXYZ(double x, double y, double z){
        if(isBariton()){
            BaritoneAPI.getProvider().getPrimaryBaritone().getCustomGoalProcess()
                .setGoalAndPath(new GoalBlock((int)x, (int)y, (int)z));
        }
    }


    @Override
    public void onInitialize() {
        LOG.info("Initializing Meteor Addon Utilities");
        GetConfigFileNSS("chat-tags", "tags.json");

        // REQUIRED: Register lambda factory
        MeteorClient.EVENT_BUS.registerLambdaFactory("net.deondree.addon", (lookupInMethod, klass) -> {
            try {
                return (MethodHandles.Lookup) lookupInMethod.invoke(null, klass, MethodHandles.lookup());
            } catch (Throwable t) {
                throw new RuntimeException(t);
            }
        });

        // Modules
        Modules.get().add(new TSRChatFilterModule());
        Modules.get().add(new TSRBetterStashFinderModule());
        Modules.get().add(new MenuModule());


        // Commands
        Commands.add(new CreditCommand());
        Commands.add(new BetterListWaypoinsCommand());
        Commands.add(new FriendCommand());

        // HUD
        Hud.get().register(FriendHUD.INFO);
    }




        @Override
    public void onRegisterCategories() {
        Modules.registerCategory(SIXBSIXT);
        Modules.registerCategory(Menu);




    }

    @Override
    public String getPackage() {
        return "net.deondree.addon";
    }

    @Override
    public GithubRepo getRepo() {
        return new GithubRepo("Deondree-dev", "Deondree-Utils");
    }
}
