//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "/Users/imac/Downloads/Minecraft-Deobfuscator3000-master/1.12 stable mappings"!

//Decompiled by Procyon!

package me.zeroeightsix.kami.setting.impl;

import me.zeroeightsix.kami.setting.*;
import me.zeroeightsix.kami.setting.converter.*;
import java.util.function.*;
import com.google.common.base.*;

public class EnumSetting<T extends Enum> extends Setting<T>
{
    private EnumConverter converter;
    public final Class<? extends Enum> clazz;
    
    public EnumSetting(final T value, final Predicate<T> restriction, final BiConsumer<T, T> consumer, final String name, final Predicate<T> visibilityPredicate, final Class<? extends Enum> clazz) {
        super(value, restriction, consumer, name, visibilityPredicate);
        this.converter = new EnumConverter(clazz, value);
        this.clazz = clazz;
    }
    
    @Override
    public Converter converter() {
        return this.converter;
    }
}
