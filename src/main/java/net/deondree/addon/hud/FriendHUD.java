package net.deondree.addon.hud;

import meteordevelopment.meteorclient.systems.hud.HudElement;
import meteordevelopment.meteorclient.systems.hud.HudElementInfo;
import meteordevelopment.meteorclient.systems.hud.HudRenderer;
import meteordevelopment.meteorclient.utils.render.color.Color;
import meteordevelopment.meteorclient.systems.friends.Friends;


import net.deondree.addon.DeondreeAddon;


import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;


import static meteordevelopment.meteorclient.MeteorClient.mc;

public class FriendHUD extends HudElement {
    List<String> AddedFriends = new ArrayList<>();
    List<String> OnlineFriends = new ArrayList<>();
    List<String> OfflineFriends = new ArrayList<>();
    double BIGGESTTEXTWIDTH = 0.0;
    public static final HudElementInfo<FriendHUD> INFO = new HudElementInfo<>(DeondreeAddon.HUD_GROUP, "example", "HUD element example.", FriendHUD::new);

    public FriendHUD() {
        super(INFO);
    }

    @Override
    public void render(HudRenderer renderer) {
        renderer.quad(x, y, getWidth(), getHeight(), Color.DARK_GRAY);
        AtomicInteger i = new AtomicInteger();
        AtomicInteger finalI = i;
        Friends.get().forEach(friend -> {
            if(renderer.textWidth(friend.name) > BIGGESTTEXTWIDTH){
                BIGGESTTEXTWIDTH = renderer.textWidth(friend.name);
            }
            finalI.getAndIncrement();
            AddedFriends.clear();
            OnlineFriends.clear();
            OfflineFriends.clear();



            boolean FriendOnline = Objects.requireNonNull(mc.getNetworkHandler())
                .getPlayerList()
                .stream()
                .anyMatch(entry -> entry.getProfile().getName().equalsIgnoreCase(friend.name));

            if(FriendOnline){
                setSize(BIGGESTTEXTWIDTH, renderer.textHeight(true));
                renderer.text(friend.name, x, y + finalI.intValue() * 10, Color.GREEN, true);
            } else {
                OfflineFriends.add(friend.name);
            }
            AddedFriends.add(friend.name);

        });
        i = new AtomicInteger();


        //setSize(renderer.textWidth((OnlineFriends).toString(), true), renderer.textHeight(true));

        // Render background


        // Render text



    }

}
