package net.deondree.addon;

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
import net.deondree.addon.hud.FriendHUD;
import net.deondree.addon.modules.MenuModule;
import net.deondree.addon.modules.TSRBetterStashFinderModule;
import net.deondree.addon.modules.TSRChatFilterModule;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ServerInfo;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.BlockPos;
import org.slf4j.Logger;
import baritone.api.BaritoneAPI;
import baritone.api.IBaritone;
import baritone.api.pathing.goals.GoalBlock;

import java.io.File;
import java.lang.invoke.MethodHandles;

public class DeondreeAddon extends MeteorAddon {
    public static final Logger LOG = LogUtils.getLogger();
    public static final Category Menu = new Category("Deondree's Utils - Menu");
    public static final Category SIXBSIXT = new Category("Deondree's Utils - 6b6t.org");

    public static final HudGroup HUD_GROUP = new HudGroup("Deondree's Utils");

    public static File GetConfigFile(String key, String filename) {
        return new File(new File(new File(new File(MeteorClient.FOLDER, "omegaware"), key), Utils.getFileWorldName()), filename);
    }
    public static File getAddonConfigfile(String key, String filename){
        return new File(new File(new File(new File(MeteorClient.FOLDER, "DeondreeUtils"), key), Utils.getFileWorldName()), filename);
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
