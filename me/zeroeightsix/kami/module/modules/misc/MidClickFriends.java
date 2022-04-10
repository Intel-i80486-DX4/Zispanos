//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "/Users/imac/Downloads/Minecraft-Deobfuscator3000-master/1.12 stable mappings"!

//Decompiled by Procyon!

package me.zeroeightsix.kami.module.modules.misc;

import me.zeroeightsix.kami.module.*;
import net.minecraftforge.fml.common.gameevent.*;
import me.zero.alpine.listener.*;
import java.util.function.*;
import me.zeroeightsix.kami.util.*;
import java.util.*;
import me.zeroeightsix.kami.command.*;
import me.zeroeightsix.kami.command.commands.*;
import org.lwjgl.input.*;
import net.minecraft.client.*;
import net.minecraft.util.math.*;
import net.minecraft.client.entity.*;
import net.minecraft.entity.*;

@Info(name = "MidClickFriends", category = Category.MISC, description = "Middle click players to friend or unfriend them", showOnArray = ShowOnArray.OFF)
public class MidClickFriends extends Module
{
    private int delay;
    @EventHandler
    public Listener<InputEvent.MouseInputEvent> mouseListener;
    
    public MidClickFriends() {
        this.delay = 0;
        this.mouseListener = (Listener<InputEvent.MouseInputEvent>)new Listener(event -> {
            if (this.delay == 0 && Mouse.getEventButton() == 2 && Minecraft.getMinecraft().objectMouseOver.typeOfHit.equals((Object)RayTraceResult.Type.ENTITY)) {
                final Entity lookedAtEntity = Minecraft.getMinecraft().objectMouseOver.entityHit;
                if (!(lookedAtEntity instanceof EntityOtherPlayerMP)) {
                    return;
                }
                if (Friends.isFriend(lookedAtEntity.getName())) {
                    this.remove(lookedAtEntity.getName());
                }
                else {
                    this.add(lookedAtEntity.getName());
                }
            }
        }, new Predicate[0]);
    }
    
    @Override
    public void onUpdate() {
        if (this.delay > 0) {
            --this.delay;
        }
    }
    
    private void remove(final String name) {
        this.delay = 20;
        final Friends.Friend friend2 = Friends.friends.getValue().stream().filter(friend1 -> friend1.getUsername().equalsIgnoreCase(name)).findFirst().get();
        Friends.friends.getValue().remove(friend2);
        Command.sendChatMessage("&b" + friend2.getUsername() + "&r has been unfriended.");
    }
    
    private void add(final String name) {
        this.delay = 20;
        final Friends.Friend f;
        new Thread(() -> {
            f = new FriendCommand().getFriendByName(name);
            if (f == null) {
                Command.sendChatMessage("Failed to find UUID of " + name);
            }
            else {
                Friends.friends.getValue().add(f);
                Command.sendChatMessage("&b" + f.getUsername() + "&r has been friended.");
            }
        }).start();
    }
}
