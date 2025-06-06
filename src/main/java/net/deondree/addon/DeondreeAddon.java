package net.deondree.addon;

import meteordevelopment.meteorclient.MeteorClient;
import meteordevelopment.meteorclient.utils.Utils;
import net.deondree.addon.commands.CommandExample;
import net.deondree.addon.hud.HudExample;
import com.mojang.logging.LogUtils;
import meteordevelopment.meteorclient.addons.GithubRepo;
import meteordevelopment.meteorclient.addons.MeteorAddon;
import meteordevelopment.meteorclient.commands.Commands;
import meteordevelopment.meteorclient.systems.hud.Hud;
import meteordevelopment.meteorclient.systems.hud.HudGroup;
import meteordevelopment.meteorclient.systems.modules.Category;
import meteordevelopment.meteorclient.systems.modules.Modules;
import net.deondree.addon.modules.TSRChatFilterModule;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ServerInfo;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.slf4j.Logger;

import java.io.File;
import java.lang.invoke.MethodHandles;

public class DeondreeAddon extends MeteorAddon {
    public static final Logger LOG = LogUtils.getLogger();
    public static final Category CATEGORY = new Category("Deondree's Utils");
    public static final HudGroup HUD_GROUP = new HudGroup("Deondree's Utils");

    public static File GetConfigFile(String key, String filename) {
        return new File(new File(new File(new File(MeteorClient.FOLDER, "omegaware"), key), Utils.getFileWorldName()), filename);
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


    @Override
    public void onInitialize() {
        LOG.info("Initializing Meteor Addon Utilities");

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


        // Commands
        Commands.add(new CommandExample());

        // HUD
        Hud.get().register(HudExample.INFO);
    }


    @Override
    public void onRegisterCategories() {
        Modules.registerCategory(CATEGORY);

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
