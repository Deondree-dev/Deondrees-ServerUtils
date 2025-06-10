package net.deondree.addon.modules;

import meteordevelopment.meteorclient.events.world.TickEvent;
import meteordevelopment.meteorclient.systems.friends.Friend;
import meteordevelopment.meteorclient.systems.friends.Friends;
import com.google.gson.JsonElement;
import com.mojang.brigadier.Message;
import meteordevelopment.meteorclient.events.game.ReceiveMessageEvent;
import meteordevelopment.meteorclient.pathing.IPathManager;
import meteordevelopment.meteorclient.settings.BoolSetting;
import meteordevelopment.meteorclient.settings.EnumSetting;
import meteordevelopment.meteorclient.settings.Setting;
import meteordevelopment.meteorclient.settings.SettingGroup;
import meteordevelopment.meteorclient.systems.friends.Friends;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.meteorclient.systems.modules.Modules;
import meteordevelopment.meteorclient.systems.modules.world.StashFinder;
import meteordevelopment.meteorclient.utils.render.color.SettingColor;
import meteordevelopment.orbit.EventHandler;
import meteordevelopment.meteorclient.systems.friends.Friend;
import meteordevelopment.meteorclient.systems.friends.Friends;
import net.deondree.addon.DeondreeAddon;
import net.minecraft.text.Style;
import net.minecraft.text.Text;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Objects;

import static java.lang.System.in;
import static meteordevelopment.meteorclient.utils.world.BlockUtils.getDirection;


public class MenuModule extends Module {
    private final SettingGroup sgGeneral = settings.getDefaultGroup();
    private final SettingGroup MCSettings = this.settings.createGroup("Minecraft-F3-Settings");
    private final SettingGroup ColorSettings = this.settings.createGroup("Chat-Friends");

    private final Setting<Boolean> AdvancedToolTips = MCSettings.add(new BoolSetting.Builder()
        .name("AdvancedToolTips")
        .description("Sets the 'Advanced Tool Tip' setting to make durability visible")
        .defaultValue(mc.options.advancedItemTooltips)
            .onChanged(mode -> {
                mc.options.advancedItemTooltips = mode;
            })
        .build()
    );
    private final Setting<Boolean> ShowCustomChat = ColorSettings.add(new BoolSetting.Builder()
        .name("Even Better Chat (Beta)")
        .description("Makes Chat even better, change friends Chat color with .tag color <name> <color>")
        .defaultValue(true)
            .onChanged(mode -> {

            })
        .build()
    );











    @Override
    public void onDeactivate() {

    }


    @EventHandler
    private void onReceiveMessage(ReceiveMessageEvent event) {
        String[] MessageArray = (event.getMessage().getString()).split(" » ");
        String message = Arrays.toString((event.getMessage().getString()).split(" » "));
        String MESSAGE = "";
        String prefixAndUsername = MessageArray[0];
        String[] RNKanName = prefixAndUsername.split(" ");
        String Name = RNKanName[RNKanName.length - 1];
        if (Objects.equals(Name, "quit")) {
            //make it turn Color.RED
        }
        Friends.get().forEach(friend -> {
            if(Objects.equals(Name, friend.name)){
                //turn a custom color:
                String color = DeondreeAddon.getColor(friend.name);
                String rawText = event.getMessage().getString();
                event.setMessage(Text.literal(rawText)
                    .setStyle(Style.EMPTY.withColor(DeondreeAddon.StringToEncodeColor(color))));
            }
        });

        //DeondreeAddon.LOG.info("Message recieved: "+(Object) (message.replace(MessageArray[0], "")).trim()+" From: " + RNKanName[RNKanName.length - 1]);

    }


    public MenuModule() {
        super(DeondreeAddon.Menu, "MinecraftUtils", "Deondrees Minecraft option Utils");
    }





}

