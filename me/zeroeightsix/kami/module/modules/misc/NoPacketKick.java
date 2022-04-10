//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "/Users/imac/Downloads/Minecraft-Deobfuscator3000-master/1.12 stable mappings"!

//Decompiled by Procyon!

package me.zeroeightsix.kami.module.modules.misc;

import me.zeroeightsix.kami.module.*;

@Module.Info(name = "NoPacketKick", category = Module.Category.MISC, description = "Prevent large packets from kicking you")
public class NoPacketKick
{
    private static NoPacketKick INSTANCE;
    
    public NoPacketKick() {
        NoPacketKick.INSTANCE = this;
    }
    
    public static boolean isEnabled() {
        final NoPacketKick instance = NoPacketKick.INSTANCE;
        return isEnabled();
    }
}
