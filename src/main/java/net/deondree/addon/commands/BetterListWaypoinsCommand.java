package net.deondree.addon.commands;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import meteordevelopment.meteorclient.commands.Command;
import meteordevelopment.meteorclient.systems.waypoints.Waypoint;
import meteordevelopment.meteorclient.systems.waypoints.Waypoints;
import meteordevelopment.meteorclient.utils.player.ChatUtils;
import meteordevelopment.meteorclient.utils.player.PlayerUtils;
import net.minecraft.command.CommandSource;
import net.minecraft.text.ClickEvent;
import net.minecraft.text.HoverEvent;
import net.minecraft.util.math.BlockPos;

import net.minecraft.text.Text;
import net.minecraft.text.Style;
import net.minecraft.util.Formatting;

public class BetterListWaypoinsCommand extends Command {
    public BetterListWaypoinsCommand() {
        super("lwp", "Adds better Waypoints/more readable");
    }
    public void build(LiteralArgumentBuilder<CommandSource> builder) {
        builder.then(literal("name").then(argument("nameArgument", StringArgumentType.word()).executes(context -> {
            String argument = StringArgumentType.getString(context, "nameArgument");

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
                Text prefix = Text.literal("[Utils] ")
                    .setStyle(Style.EMPTY.withColor(Formatting.GOLD));

                Text name = Text.literal("[" + waypoint.name + "]")
                    .setStyle(Style.EMPTY
                        .withColor(Formatting.AQUA)
                        .withClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/yourCommandHere " + waypoint.name))
                        .withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, Text.literal("Goto pos")))
                    );

                String coords = ": " + waypoint.pos.get().getX() + ", " + waypoint.pos.get().getY() + ", " + waypoint.pos.get().getZ();

                Text fullMessage = Text.empty().append(prefix).append(name).append(Text.literal(coords));

                ChatUtils.sendMsg(fullMessage);
                // Text.of(String.format("(%.0f, %.0f, %.0f)", (double) waypoint.pos.get().getX(), (double) waypoint.pos.get().getY(), (double) waypoint.pos.get().getZ()));

            }


            return SINGLE_SUCCESS;
        }));
    }
}
