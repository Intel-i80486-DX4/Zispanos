//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "/Users/imac/Downloads/Minecraft-Deobfuscator3000-master/1.12 stable mappings"!

//Decompiled by Procyon!

package me.zeroeightsix.kami.setting.builder.numerical;

import me.zeroeightsix.kami.setting.builder.*;
import java.util.function.*;
import me.zeroeightsix.kami.setting.impl.numerical.*;
import me.zeroeightsix.kami.setting.*;

public abstract class NumericalSettingBuilder<T extends Number> extends SettingBuilder<T>
{
    protected T min;
    protected T max;
    
    public NumericalSettingBuilder<T> withMinimum(final T minimum) {
        this.predicateList.add(t -> t.doubleValue() >= minimum.doubleValue());
        if (this.min == null || minimum.doubleValue() > this.min.doubleValue()) {
            this.min = minimum;
        }
        return this;
    }
    
    public NumericalSettingBuilder<T> withMaximum(final T maximum) {
        this.predicateList.add(t -> t.doubleValue() <= maximum.doubleValue());
        if (this.max == null || maximum.doubleValue() < this.max.doubleValue()) {
            this.max = maximum;
        }
        return this;
    }
    
    public NumericalSettingBuilder<T> withRange(final T minimum, final T maximum) {
        final double doubleValue;
        this.predicateList.add(t -> {
            doubleValue = t.doubleValue();
            return doubleValue >= minimum.doubleValue() && doubleValue <= maximum.doubleValue();
        });
        if (this.min == null || minimum.doubleValue() > this.min.doubleValue()) {
            this.min = minimum;
        }
        if (this.max == null || maximum.doubleValue() < this.max.doubleValue()) {
            this.max = maximum;
        }
        return this;
    }
    
    public NumericalSettingBuilder<T> withListener(final BiConsumer<T, T> consumer) {
        this.consumer = consumer;
        return this;
    }
    
    public NumericalSettingBuilder<T> withValue(final T value) {
        return (NumericalSettingBuilder)super.withValue((Object)value);
    }
    
    public NumericalSettingBuilder withName(final String name) {
        return (NumericalSettingBuilder)super.withName(name);
    }
    
    public abstract NumberSetting build();
}
