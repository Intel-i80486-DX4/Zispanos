//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "/Users/imac/Downloads/Minecraft-Deobfuscator3000-master/1.12 stable mappings"!

//Decompiled by Procyon!

package me.zeroeightsix.kami.setting.converter;

import com.google.gson.*;

public class BoxedFloatConverter extends AbstractBoxedNumberConverter<Float>
{
    protected Float doBackward(final JsonElement s) {
        return s.getAsFloat();
    }
}
