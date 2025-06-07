package net.deondree.addon.modules;

import meteordevelopment.meteorclient.pathing.IPathManager;
import meteordevelopment.meteorclient.settings.BoolSetting;
import meteordevelopment.meteorclient.settings.EnumSetting;
import meteordevelopment.meteorclient.settings.Setting;
import meteordevelopment.meteorclient.settings.SettingGroup;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.meteorclient.systems.modules.Modules;
import meteordevelopment.meteorclient.systems.modules.world.StashFinder;
import net.deondree.addon.DeondreeAddon;

import static meteordevelopment.meteorclient.utils.world.BlockUtils.getDirection;


public class MenuModule extends Module {
    private final SettingGroup sgGeneral = settings.getDefaultGroup();
    private final SettingGroup MCSettings = this.settings.createGroup("Minecraft-F3-Settings");

    private final Setting<Boolean> AdvancedToolTips = MCSettings.add(new BoolSetting.Builder()
        .name("AdvancedToolTips")
        .description("Sets the 'Advanced Tool Tip' setting to make durability visible")
        .defaultValue(mc.options.advancedItemTooltips)
            .onChanged(mode -> {
                mc.options.advancedItemTooltips = mode;
            })
        .build()
    );






    @Override
    public void onDeactivate() {

    }





    public MenuModule() {
        super(DeondreeAddon.Menu, "MinecraftUtils", "Deondrees Minecraft option Utils");
    }
}

