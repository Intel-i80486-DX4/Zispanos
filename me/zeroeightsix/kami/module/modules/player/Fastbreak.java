//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "/Users/imac/Downloads/Minecraft-Deobfuscator3000-master/1.12 stable mappings"!

//Decompiled by Procyon!

package me.zeroeightsix.kami.module.modules.player;

import me.zeroeightsix.kami.module.*;

@Info(name = "Fastbreak", category = Category.HIDDEN, description = "Nullifies block hit delay")
public class Fastbreak extends Module
{
    @Override
    public void onUpdate() {
        Fastbreak.mc.playerController.blockHitDelay = 0;
    }
}
