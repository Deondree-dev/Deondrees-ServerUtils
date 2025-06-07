package net.deondree.addon.hud;

import meteordevelopment.meteorclient.systems.hud.HudElement;
import meteordevelopment.meteorclient.systems.hud.HudElementInfo;
import meteordevelopment.meteorclient.systems.hud.HudRenderer;
import meteordevelopment.meteorclient.utils.render.color.Color;
import meteordevelopment.meteorclient.systems.friends.Friends;
import net.deondree.addon.DeondreeAddon;

import java.util.ArrayList;
import java.util.List;

import static meteordevelopment.meteorclient.MeteorClient.mc;

public class FriendHUD extends HudElement {
    private final List<String> onlineFriends = new ArrayList<>();
    private final List<String> offlineFriends = new ArrayList<>();

    public static final HudElementInfo<FriendHUD> INFO = new HudElementInfo<>(DeondreeAddon.HUD_GROUP, "friend-hud", "Shows online and offline friends.", FriendHUD::new);

    public FriendHUD() {
        super(INFO);
    }

    @Override
    public void render(HudRenderer renderer) {



        if (mc.player == null || mc.getNetworkHandler() == null) {
            setSize(renderer.textWidth("Friend Hud"), renderer.textHeight(true));
            renderer.quad(x, y, getWidth(), getHeight(), Color.fromHsv(0, 0, 0.06));
            renderer.text("Friend Hud", x, y, Color.GREEN, true);
            return;
        }

        // Clear previous frame's data
        onlineFriends.clear();
        offlineFriends.clear();

        // Split friends into online/offline
        Friends.get().forEach(friend -> {
            boolean isOnline = mc.getNetworkHandler().getPlayerList().stream()
                .anyMatch(entry -> entry.getProfile().getName().equalsIgnoreCase(friend.name));

            if (isOnline) onlineFriends.add(friend.name);
            else offlineFriends.add(friend.name);
        });

        // Determine width and height

        double offWidest = 0;
        for (int j = 0; j < offlineFriends.size(); j++) {
            String friend = offlineFriends.get(j);
            if (renderer.textWidth(friend) > offWidest) {
                offWidest = renderer.textWidth(friend);
            }
        }


        double Onwidest = 0;
        for (int j = 0; j < onlineFriends.size(); j++) {
            String friend = onlineFriends.get(j);
            if (renderer.textWidth(friend) > Onwidest) {
                Onwidest = renderer.textWidth(friend);
            }
        }
        double widest = Onwidest + offWidest;
        int lineHeight = (int) renderer.textHeight(true);
        int totalHeight = Math.max(onlineFriends.size(), offlineFriends.size()) * lineHeight;
        setSize(widest * 2 + 10, totalHeight);

        // Draw background
        renderer.quad(x, y, getWidth(), getHeight(), Color.fromHsv(0, 0, 1));

        // Render online names (left column)
        for (int i = 0; i < onlineFriends.size(); i++) {
            renderer.text(onlineFriends.get(i), x, y + i * lineHeight, Color.GREEN, true);
        }

        // Render offline names (right column)
        for (int i = 0; i < offlineFriends.size(); i++) {
            renderer.text(offlineFriends.get(i), x + widest + 10, y + i * lineHeight, Color.RED, true);
        }
    }


}
