package net.deondree.addon.commands;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import meteordevelopment.meteorclient.commands.Command;
import meteordevelopment.meteorclient.systems.waypoints.Waypoint;
import meteordevelopment.meteorclient.systems.waypoints.Waypoints;
import meteordevelopment.meteorclient.utils.player.ChatUtils;
import meteordevelopment.meteorclient.utils.player.PlayerUtils;
import net.deondree.addon.modules.MenuModule;
import net.minecraft.command.CommandSource;
import net.minecraft.network.message.SentMessage;
import net.minecraft.text.ClickEvent;
import net.minecraft.text.HoverEvent;
import net.minecraft.util.ColorCode;
import net.minecraft.util.math.BlockPos;

import net.deondree.addon.*;
import net.minecraft.text.Text;
import net.minecraft.text.Style;
import net.minecraft.util.Formatting;

import java.util.Objects;

public class BetterListWaypoinsCommand extends Command {
    public BetterListWaypoinsCommand() {
        super("lwp", "Adds better Waypoints/more readable");
    }
    public void build(LiteralArgumentBuilder<CommandSource> builder) {
        builder.then(literal("make").then(argument("Name", StringArgumentType.word()).executes(context -> {
            String argument = StringArgumentType.getString(context, "Name");

            if (mc.player == null) return -1;

            Waypoint waypoint = new Waypoint.Builder()
                .name(argument)
                .pos(new BlockPos((int) mc.player.getX(), (int) mc.player.getY() + 2, (int) mc.player.getZ()))
                .dimension(PlayerUtils.getDimension())
                .build();

            Waypoints.get().add(waypoint);
            ChatUtils.info("[§6Utils§f] §f[§bMade§f]: '§b" + argument + "§f'");
            return SINGLE_SUCCESS;
        })));
        builder.then(literal("list").executes(context -> {
            Waypoints.get();
            for (Waypoint waypoint : Waypoints.get()) {
                Text prefix = Text.literal("§f[§6Utils§f] ");

                Text name = Text.literal(String.valueOf(waypoint.name))
                    .setStyle(Style.EMPTY
                        .withColor(Formatting.AQUA)
                        .withClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "#goto " +
                            waypoint.pos.get().getX() + " " +
                            waypoint.pos.get().getY() + " " +
                            waypoint.pos.get().getZ()))
                        .withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, Text.literal("Click to go to waypoint")))
                    );

                Text coords = Text.literal(": " + waypoint.pos.get().getX() + ", " + waypoint.pos.get().getY() + ", " + waypoint.pos.get().getZ());

                Text del = Text.literal(" §4[DEL]§r")
                    .setStyle(Style.EMPTY
                        .withColor(Formatting.AQUA)
                        .withClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, ".waypoint delete " + waypoint.name.get()))
                        .withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, Text.literal(".lwp delete " + waypoint.name.get())))
                    );


                Text fullMessage = Text.empty()
                        .append(prefix)
                        .append(name)
                        .append(coords);

                ChatUtils.sendMsg(fullMessage);


                // Text.of(String.format("(%.0f, %.0f, %.0f)", (double) waypoint.pos.get().getX(), (double) waypoint.pos.get().getY(), (double) waypoint.pos.get().getZ()));

            }


            return SINGLE_SUCCESS;
        }));
        builder.then(literal("delete").then(argument("waypoint", StringArgumentType.word()).executes(context -> {
            String argument = StringArgumentType.getString(context, "waypoint");
            boolean FOUND = false;

            for (Waypoint waypoint : Waypoints.get()) {
                if (waypoint.name.get().equalsIgnoreCase(argument)) {
                    FOUND = true;
                    Waypoints.get().remove(waypoint);
                    ChatUtils.sendMsg(Text.literal("[§6Utils§f] Successfully Deleted ")
                        .append(argument).setStyle(Style.EMPTY
                            .withColor(Formatting.AQUA))

                    );
                    break;
                }
            }
            if(!FOUND){
                ChatUtils.sendMsg(Text.literal("[§6Utils§f] ")
                    .append(argument).setStyle(Style.EMPTY
                        .withColor(Formatting.AQUA))
                    .append(Text.literal("Failed to delete, §4NOTFOUND"))
                );

            }


            return SINGLE_SUCCESS;
        })));
    }
}
