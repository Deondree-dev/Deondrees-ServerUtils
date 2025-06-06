package net.deondree.addon.commands;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import meteordevelopment.meteorclient.commands.Command;
import net.minecraft.command.CommandSource;

/**
 * The Meteor Client command API uses the <a href="https://github.com/Mojang/brigadier">same command system as Minecraft does</a>.
 */
public class CreditCommand extends Command {
    /**
     * The {@code name} parameter should be in kebab-case.
     */
    public CreditCommand() {
        super("credits", "List Projects that helped me build this.");
    }

    @Override
    public void build(LiteralArgumentBuilder<CommandSource> builder) {
        builder.executes(context -> {
            info("§l§e6B§b6T§f§r: §bOmegaware §7by §3omega172 §7& §bPowie69");
            return SINGLE_SUCCESS;
        });
    }
}
