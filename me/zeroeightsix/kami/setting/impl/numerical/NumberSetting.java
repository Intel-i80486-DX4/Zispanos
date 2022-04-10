//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "/Users/imac/Downloads/Minecraft-Deobfuscator3000-master/1.12 stable mappings"!

//Decompiled by Procyon!

package me.zeroeightsix.kami.setting.impl.numerical;

import me.zeroeightsix.kami.setting.*;
import java.util.function.*;
import me.zeroeightsix.kami.setting.converter.*;
import com.google.common.base.*;

public abstract class NumberSetting<T extends Number> extends Setting<T>
{
    private final T min;
    private final T max;
    
    public NumberSetting(final T value, final Predicate<T> restriction, final BiConsumer<T, T> consumer, final String name, final Predicate<T> visibilityPredicate, final T min, final T max) {
        super(value, restriction, consumer, name, visibilityPredicate);
        this.min = min;
        this.max = max;
    }
    
    public boolean isBound() {
        return this.min != null && this.max != null;
    }
    
    @Override
    public abstract AbstractBoxedNumberConverter converter();
    
    @Override
    public T getValue() {
        return super.getValue();
    }
    
    public T getMax() {
        return this.max;
    }
    
    public T getMin() {
        return this.min;
    }
}
