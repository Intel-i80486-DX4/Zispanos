//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "/Users/imac/Downloads/Minecraft-Deobfuscator3000-master/1.12 stable mappings"!

//Decompiled by Procyon!

package me.zeroeightsix.kami.module.modules.chat;

import net.minecraft.entity.player.*;
import net.minecraftforge.event.entity.player.*;
import me.zero.alpine.listener.*;
import me.zeroeightsix.kami.event.events.*;
import me.zeroeightsix.kami.setting.*;
import java.util.function.*;
import me.zeroeightsix.kami.module.*;
import java.util.*;
import net.minecraft.client.gui.*;

@Info(name = "AutoEZ", category = Category.COMBAT, description = "Sends an insult in chat after killing someone")
public class AutoEZ extends Module
{
    private Setting<Mode> mode;
    EntityPlayer focus;
    int hasBeenCombat;
    @EventHandler
    public Listener<AttackEntityEvent> livingDeathEventListener;
    @EventHandler
    public Listener<GuiScreenEvent.Displayed> listener;
    
    public AutoEZ() {
        this.mode = this.register(Settings.e("Mode", Mode.ONTOP));
        this.livingDeathEventListener = (Listener<AttackEntityEvent>)new Listener(event -> {
            if (event.getTarget() instanceof EntityPlayer) {
                this.focus = (EntityPlayer)event.getTarget();
                if (event.getEntityPlayer().getUniqueID() == AutoEZ.mc.player.getUniqueID()) {
                    if (this.focus.getHealth() <= 0.0 || this.focus.isDead || !AutoEZ.mc.world.playerEntities.contains(this.focus)) {
                        AutoEZ.mc.player.sendChatMessage(this.getText(this.mode.getValue()) + event.getTarget().getName());
                        return;
                    }
                    this.hasBeenCombat = 1000;
                }
            }
        }, new Predicate[0]);
        this.listener = (Listener<GuiScreenEvent.Displayed>)new Listener(event -> {
            if (!(event.getScreen() instanceof GuiGameOver)) {
                return;
            }
            if (AutoEZ.mc.player.getHealth() > 0.0f) {
                this.hasBeenCombat = 0;
            }
        }, new Predicate[0]);
    }
    
    private String getText(final Mode m) {
        return m.text;
    }
    
    @Override
    public void onUpdate() {
        if (this.hasBeenCombat > 0 && (this.focus.getHealth() <= 0.0f || this.focus.isDead || !AutoEZ.mc.world.playerEntities.contains(this.focus))) {
            if (ModuleManager.getModuleByName("AutoEZ").isEnabled()) {
                final Random rand = new Random();
                final int randomNum = rand.nextInt(6) + 1;
                if (randomNum == 1) {
                    AutoEZ.mc.player.sendChatMessage("You just got FUCKED thanks to ZISPANOS BACKDOOR, " + this.focus.getName());
                }
                if (randomNum == 2) {
                    AutoEZ.mc.player.sendChatMessage("ZISPANOS ONTOP FOREVER AND ALWAYS!, EZ, " + this.focus.getName());
                }
                if (randomNum == 4) {
                    AutoEZ.mc.player.sendChatMessage("I just removed a hole camping faggot thanks to ZISPANOS BACKDOOR. Don't even bother coming back, " + this.focus.getName());
                }
                if (randomNum == 3) {
                    AutoEZ.mc.player.sendChatMessage("Everybody whip and naenae; I just killed " + this.focus.getName() + " With ZISPANOS BACKDOOR!!!");
                }
                if (randomNum == 5) {
                    AutoEZ.mc.player.sendChatMessage("ZISPANOS forever ontop! Thanks for the clip, " + this.focus.getName());
                }
                if (randomNum == 6) {
                    AutoEZ.mc.player.sendChatMessage("CLOWN DOWN " + this.focus.getName() + "! You just got fucked with ZISPANOS BACKDOOR!");
                }
            }
            this.hasBeenCombat = 0;
        }
        --this.hasBeenCombat;
    }
    
    public enum Mode
    {
        FUCKED("You just got FUCKED thanks to DVDGOD.GG, "), 
        ONTOP("DVDGOD ONTOP FOREVER AND ALWAYS!, EZ, "), 
        REMOVED("I just removed a hole camping faggot thanks to DVDGOD.GG. Don't even bother coming back, "), 
        NAENAE("Everybody whip and naenae, Using DVDGOD.GG I just killed "), 
        CLIPPED("DVDGOD.GG forever ontop! Thanks for the clip, ");
        
        private String text;
        
        private Mode(final String text) {
            this.text = text;
        }
    }
}
