//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "/Users/imac/Downloads/Minecraft-Deobfuscator3000-master/1.12 stable mappings"!

//Decompiled by Procyon!

package me.zeroeightsix.kami.module.modules.combat;

import me.zeroeightsix.kami.module.*;
import me.zeroeightsix.kami.event.events.*;
import me.zero.alpine.listener.*;
import java.util.function.*;
import net.minecraft.client.entity.*;
import me.zeroeightsix.kami.util.*;
import net.minecraft.entity.*;

@Info(name = "AntiFriendHit", description = "Don't hit your friends", category = Category.COMBAT, alwaysListening = true)
public class AntiFriendHit extends Module
{
    @EventHandler
    Listener<ClientPlayerAttackEvent> listener;
    
    public AntiFriendHit() {
        this.listener = (Listener<ClientPlayerAttackEvent>)new Listener(event -> {
            if (this.isDisabled()) {
                return;
            }
            final Entity e = AntiFriendHit.mc.objectMouseOver.entityHit;
            if (e instanceof EntityOtherPlayerMP && Friends.isFriend(e.getName())) {
                event.cancel();
            }
        }, new Predicate[0]);
    }
}
