//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "/Users/imac/Downloads/Minecraft-Deobfuscator3000-master/1.12 stable mappings"!

//Decompiled by Procyon!

package me.zeroeightsix.kami.module.modules.movement;

import me.zeroeightsix.kami.module.*;

@Info(name = "AutoJump", category = Category.HIDDEN, description = "Automatically jumps if possible")
public class AutoJump extends Module
{
    @Override
    public void onUpdate() {
        if (AutoJump.mc.player.isInWater() || AutoJump.mc.player.isInLava()) {
            AutoJump.mc.player.motionY = 0.1;
        }
        else if (AutoJump.mc.player.onGround) {
            AutoJump.mc.player.jump();
        }
    }
}
