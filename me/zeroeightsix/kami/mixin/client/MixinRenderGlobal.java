//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "/Users/imac/Downloads/Minecraft-Deobfuscator3000-master/1.12 stable mappings"!

//Decompiled by Procyon!

package me.zeroeightsix.kami.mixin.client;

import net.minecraft.client.*;
import org.spongepowered.asm.mixin.*;
import net.minecraft.client.renderer.*;

@Mixin({ RenderGlobal.class })
public class MixinRenderGlobal
{
    @Shadow
    Minecraft mc;
    @Shadow
    public ChunkRenderContainer renderContainer;
}
