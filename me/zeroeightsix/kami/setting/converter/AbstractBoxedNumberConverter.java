//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "/Users/imac/Downloads/Minecraft-Deobfuscator3000-master/1.12 stable mappings"!

//Decompiled by Procyon!

package me.zeroeightsix.kami.setting.converter;

import com.google.common.base.*;
import com.google.gson.*;

public abstract class AbstractBoxedNumberConverter<T extends Number> extends Converter<T, JsonElement>
{
    protected JsonElement doForward(final T t) {
        return (JsonElement)new JsonPrimitive((Number)t);
    }
}
