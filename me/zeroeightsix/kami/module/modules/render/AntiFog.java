//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "/Users/imac/Downloads/Minecraft-Deobfuscator3000-master/1.12 stable mappings"!

//Decompiled by Procyon!

package me.zeroeightsix.kami.module.modules.render;

import me.zeroeightsix.kami.module.*;
import me.zeroeightsix.kami.setting.*;

@Info(name = "AntiFog", description = "Disables or reduces fog", category = Category.RENDER)
public class AntiFog extends Module
{
    public static Setting<VisionMode> mode;
    private static AntiFog INSTANCE;
    
    public AntiFog() {
        (AntiFog.INSTANCE = this).register(AntiFog.mode);
    }
    
    public static boolean enabled() {
        return AntiFog.INSTANCE.isEnabled();
    }
    
    static {
        AntiFog.mode = Settings.e("Mode", VisionMode.NOFOG);
        AntiFog.INSTANCE = new AntiFog();
    }
    
    public enum VisionMode
    {
        NOFOG, 
        AIR;
    }
}
