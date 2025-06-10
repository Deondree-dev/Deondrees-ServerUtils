package net.deondree.addon.commands;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import meteordevelopment.meteorclient.commands.Command;
import meteordevelopment.meteorclient.systems.friends.Friend;
import meteordevelopment.meteorclient.systems.friends.Friends;
import net.deondree.addon.DeondreeAddon;
import net.minecraft.command.CommandSource;

import java.io.File;
import java.io.FileWriter;
import java.io.Writer;
import java.nio.file.Files;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.Gson;


import static com.mojang.brigadier.arguments.StringArgumentType.word;
import static com.mojang.brigadier.arguments.StringArgumentType.getString;
import static com.mojang.brigadier.builder.LiteralArgumentBuilder.literal;
import static com.mojang.brigadier.builder.RequiredArgumentBuilder.argument;


public class FriendCommand extends Command {
    public FriendCommand() {
        super("tag", "Extension to FriendHUD");
    }

    public static void addTagTo(String name, String key, String value) {
        File configFile = DeondreeAddon.GetConfigFileNSS("chat-tags", "tag-" + name + ".json");

        try {
            configFile.getParentFile().mkdirs(); // Ensure directory structure

            JsonObject payload;

            if (configFile.exists()) {
                try {
                    String content = Files.readString(configFile.toPath()).trim();

                    if (!content.isEmpty() && !content.equalsIgnoreCase("null")) {
                        JsonElement parsed = JsonParser.parseString(content);
                        if (parsed.isJsonObject()) {
                            payload = parsed.getAsJsonObject();
                        } else {
                            payload = new JsonObject(); // fallback if malformed
                        }
                    } else {
                        payload = new JsonObject(); // empty or "null"
                    }
                } catch (Exception e) {
                    payload = new JsonObject(); // corrupted JSON fallback
                }
            } else {
                payload = new JsonObject(); // file doesn't exist
            }

            // Set the key-value
            payload.addProperty(key, value);

            // Save updated file
            try (Writer writer = new FileWriter(configFile)) {
                new Gson().toJson(payload, writer);
            }

            DeondreeAddon.LOG.info("Saved tag '{}'='{}' for {}", key, value, name);
        } catch (Exception e) {
            DeondreeAddon.LOG.error("Failed to save tag to {}: {}", configFile.toPath(), e.getMessage());
        }
    }



    @Override
    public void build(LiteralArgumentBuilder<CommandSource> builder) {
        builder.then(literal("makeTag")
            .then(argument("friend", word())
                .suggests((ctx, suggestionBuilder) -> {
                    for (Friend f : Friends.get().get()) {
                        suggestionBuilder.suggest(f.name);
                    }
                    return suggestionBuilder.buildFuture();
                })
                .then(argument("tag", word())
                    .executes(context -> {
                        String name = getString(context, "friend");
                        String tag = getString(context, "tag");

                        info("Tagged " + name + " as: " + tag);

                        addTagTo(name, "tag", tag);

                        return SINGLE_SUCCESS;
                    }
                )
            )
        ));

        builder.then(literal("color")
            .then(argument("friend", word())
                .suggests((ctx, suggestionBuilder) -> {
                    for (Friend f : Friends.get().get()) {
                        suggestionBuilder.suggest(f.name);
                    }
                    return suggestionBuilder.buildFuture();
                })
                .then(argument("color", word())
                    .suggests((ctx, suggestionBuilder) -> {

                        return suggestionBuilder.buildFuture();
                    }).executes(context -> {
                        String name = getString(context, "friend");
                        String tag = getString(context, "color");

                        info("Color added to " + name + " COLOR: " + tag);

                        addTagTo(name, "color", tag);

                        return SINGLE_SUCCESS;
                    }
            ))));

        builder.then(literal("clearTag").then(argument("friend", word())
            .suggests((ctx, suggestionBuilder) -> {
                for (Friend f : Friends.get().get()) {
                    suggestionBuilder.suggest(f.name);
                }
                return suggestionBuilder.buildFuture();
            })));

    }
}
