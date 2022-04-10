//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "/Users/imac/Downloads/Minecraft-Deobfuscator3000-master/1.12 stable mappings"!

//Decompiled by Procyon!

package me.zeroeightsix.kami.module.modules.combat;

import me.zeroeightsix.kami.module.*;
import net.minecraft.entity.player.*;
import net.minecraft.client.*;
import me.zeroeightsix.kami.setting.*;
import net.minecraft.init.*;
import me.zeroeightsix.kami.command.*;
import java.util.*;

@Info(name = "StrengthDetect", category = Category.COMBAT, description = "Hits entities around you")
public class StrengthDetect extends Module
{
    private Setting<Boolean> watermark;
    private Setting<Boolean> color;
    private Set<EntityPlayer> str;
    public static final Minecraft mc;
    
    public StrengthDetect() {
        this.watermark = this.register(Settings.b("Watermark", true));
        this.color = this.register(Settings.b("Color", false));
        this.str = Collections.newSetFromMap(new WeakHashMap<EntityPlayer, Boolean>());
    }
    
    @Override
    public void onUpdate() {
        for (final EntityPlayer player : StrengthDetect.mc.world.playerEntities) {
            if (player.equals((Object)StrengthDetect.mc.player)) {
                continue;
            }
            if (player.isPotionActive(MobEffects.STRENGTH) && !this.str.contains(player)) {
                if (this.watermark.getValue()) {
                    if (this.color.getValue()) {
                        Command.sendChatMessage(player.getDisplayNameString() + " drank strength");
                    }
                    else {
                        Command.sendChatMessage(player.getDisplayNameString() + " drank strength");
                    }
                }
                else if (this.color.getValue()) {
                    Command.sendRawChatMessage(player.getDisplayNameString() + " drank strength");
                }
                else {
                    Command.sendRawChatMessage(player.getDisplayNameString() + " drank strength");
                }
                this.str.add(player);
            }
            if (!this.str.contains(player)) {
                continue;
            }
            if (player.isPotionActive(MobEffects.STRENGTH)) {
                continue;
            }
            if (this.watermark.getValue()) {
                if (this.color.getValue()) {
                    Command.sendChatMessage(player.getDisplayNameString() + " ran out of strength");
                }
                else {
                    Command.sendChatMessage(player.getDisplayNameString() + " ran out of strength");
                }
            }
            else if (this.color.getValue()) {
                Command.sendRawChatMessage(player.getDisplayNameString() + " ran out of strength");
            }
            else {
                Command.sendRawChatMessage(player.getDisplayNameString() + " ran out of strength");
            }
            this.str.remove(player);
        }
    }
    
    static {
        mc = Minecraft.getMinecraft();
    }
}
