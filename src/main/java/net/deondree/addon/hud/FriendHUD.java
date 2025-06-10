package net.deondree.addon.hud;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import meteordevelopment.meteorclient.settings.Setting;
import meteordevelopment.meteorclient.settings.BoolSetting;
import meteordevelopment.meteorclient.settings.ColorSetting;
import meteordevelopment.meteorclient.settings.SettingGroup;
import meteordevelopment.meteorclient.systems.friends.Friends;
import meteordevelopment.meteorclient.systems.hud.HudElement;
import meteordevelopment.meteorclient.systems.hud.HudElementInfo;
import meteordevelopment.meteorclient.systems.hud.HudRenderer;
import meteordevelopment.meteorclient.utils.render.color.Color;
import meteordevelopment.meteorclient.utils.render.color.SettingColor;
import net.deondree.addon.DeondreeAddon;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

import static meteordevelopment.meteorclient.MeteorClient.mc;

public class FriendHUD extends HudElement {
    private final SettingGroup sgGeneral = settings.getDefaultGroup();

    private final Setting<SettingColor> titleColor = sgGeneral.add(new ColorSetting.Builder()
        .name("Title Color")
        .description("Color of the HUD title.")
        .defaultValue(new Color(255, 255, 255, 180))
        .build());

    private final Setting<Boolean> tags = sgGeneral.add(new BoolSetting.Builder()
        .name("Tags")
        .description("Enable or disable name tags.")
        .defaultValue(true)
        .build());

    public static final HudElementInfo<FriendHUD> INFO = new HudElementInfo<>(
        DeondreeAddon.HUD_GROUP, "friend-hud", "Shows online and offline friends.", FriendHUD::new
    );

    private final List<String> onlineFriends = new ArrayList<>();
    private final List<String> offlineFriends = new ArrayList<>();

    public FriendHUD() {
        super(INFO);
    }

    private boolean hasTag(String name) {
        return DeondreeAddon.GetConfigFileNSS("chat-tags", "tag-" + name + ".json").exists();
    }

    private String getTag(String name) {
        File file = DeondreeAddon.GetConfigFileNSS("chat-tags", "tag-" + name + ".json");
        if (!file.exists()) return null;

        try {
            String content = Files.readString(file.toPath());
            JsonElement element = JsonParser.parseString(content);
            if (element == null || !element.isJsonObject()) return null;

            JsonObject obj = element.getAsJsonObject();
            if (!obj.has("tag")) return null;

            JsonElement tagElement = obj.get("tag");
            return tagElement.isJsonPrimitive() ? tagElement.getAsString() : null;
        } catch (IOException e) {
            DeondreeAddon.LOG.error("Failed to read tag for {}: {}", name, e.getMessage());
            return null;
        }
    }


    private String formatName(String name) {
        if (!tags.get()) return name;

        String tag = getTag(name);
        return tag != null ? name + " (" + tag + ")" : name;
    }

    @Override
    public void render(HudRenderer renderer) {
        final int titleHeight = 20;
        final int textStartY = y + titleHeight;

        if (mc.player == null || mc.getNetworkHandler() == null) {
            drawTitle(renderer, "Friend Hud");
            List<String> friendsList = new ArrayList<>();
            Friends.get().forEach(f -> friendsList.add(formatName(f.name)));
            drawFriendList(renderer, friendsList, textStartY, Color.RED);
            return;
        }

        onlineFriends.clear();
        offlineFriends.clear();

        Friends.get().forEach(friend -> {
            boolean isOnline = mc.getNetworkHandler().getPlayerList().stream()
                .anyMatch(entry -> entry.getProfile().getName().equalsIgnoreCase(friend.name));

            if (isOnline) onlineFriends.add(formatName(friend.name));
            else offlineFriends.add(formatName(friend.name));
        });

        drawTitle(renderer, "Friends List");

        int lineHeight = (int) renderer.textHeight(true);
        int totalHeight = Math.max(onlineFriends.size(), offlineFriends.size()) * lineHeight;

        double widestOnline = getMaxWidth(renderer, onlineFriends);
        double widestOffline = getMaxWidth(renderer, offlineFriends);
        double totalWidth = widestOnline + widestOffline + 10;

        setSize(totalWidth, totalHeight + titleHeight);

        renderer.quad(x, textStartY, getWidth(), getHeight() - titleHeight, new Color(25, 25, 25, (int) (0.94 * 255)));
        renderer.quad(x, y, getWidth(), titleHeight, new Color(44, 44, 44));

        for (int i = 0; i < onlineFriends.size(); i++) {
            renderer.text(onlineFriends.get(i), x, textStartY + i * lineHeight, Color.GREEN, true);
        }

        for (int i = 0; i < offlineFriends.size(); i++) {
            renderer.text(offlineFriends.get(i), x + widestOnline + 10, textStartY + i * lineHeight, Color.RED, true);
        }
    }

    private void drawTitle(HudRenderer renderer, String title) {
        int titleHeight = 20;
        double titleWidth = renderer.textWidth(title);
        renderer.quad(x, y, getWidth(), titleHeight, new Color(44, 44, 44));
        renderer.text(title, x + (getWidth() / 2.0) - (titleWidth / 2.0), y, titleColor.get(), true);
    }

    private void drawFriendList(HudRenderer renderer, List<String> names, int startY, Color color) {
        double widest = getMaxWidth(renderer, names);
        int lineHeight = (int) renderer.textHeight(true);

        setSize(widest + 10, names.size() * lineHeight + 20);
        renderer.quad(x, startY, getWidth(), getHeight() - 20, new Color(25, 25, 25, (int) (0.94 * 255)));

        for (int i = 0; i < names.size(); i++) {
            renderer.text(names.get(i), x + 10, startY + i * lineHeight, color, true);
        }
    }

    private double getMaxWidth(HudRenderer renderer, List<String> strings) {
        double width = 0;
        for (String s : strings) width = Math.max(width, renderer.textWidth(s));
        return width;
    }
}
