package net.deondree.addon;

import meteordevelopment.meteorclient.MeteorClient;
import net.deondree.addon.commands.CommandExample;
import net.deondree.addon.hud.HudExample;
import net.deondree.addon.modules.ModuleExample;
import com.mojang.logging.LogUtils;
import meteordevelopment.meteorclient.addons.GithubRepo;
import meteordevelopment.meteorclient.addons.MeteorAddon;
import meteordevelopment.meteorclient.commands.Commands;
import meteordevelopment.meteorclient.systems.hud.Hud;
import meteordevelopment.meteorclient.systems.hud.HudGroup;
import meteordevelopment.meteorclient.systems.modules.Category;
import meteordevelopment.meteorclient.systems.modules.Modules;
import org.slf4j.Logger;

import java.lang.invoke.MethodHandles;

public class DeondreeAddon extends MeteorAddon {
    public static final Logger LOG = LogUtils.getLogger();
    public static final Category CATEGORY = new Category("Example");
    public static final HudGroup HUD_GROUP = new HudGroup("Example");

    @Override
    public void onInitialize() {
        LOG.info("Initializing Meteor Addon Template");

        // REQUIRED: Register lambda factory
        MeteorClient.EVENT_BUS.registerLambdaFactory("net.deondree.addon", (lookupInMethod, klass) -> {
            try {
                return (MethodHandles.Lookup) lookupInMethod.invoke(null, klass, MethodHandles.lookup());
            } catch (Throwable t) {
                throw new RuntimeException(t);
            }
        });

        // Modules
        Modules.get().add(new ModuleExample());

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
        return new GithubRepo("MeteorDevelopment", "meteor-addon-template");
    }
}
