//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "/Users/imac/Downloads/Minecraft-Deobfuscator3000-master/1.12 stable mappings"!

//Decompiled by Procyon!

package me.zeroeightsix.kami.setting.builder.numerical;

import me.zeroeightsix.kami.setting.impl.numerical.*;
import java.util.function.*;
import me.zeroeightsix.kami.setting.*;
import me.zeroeightsix.kami.setting.builder.*;

public class FloatSettingBuilder extends NumericalSettingBuilder<Float>
{
    @Override
    public NumberSetting build() {
        return (NumberSetting)new FloatSetting((Float)this.initialValue, this.predicate(), this.consumer(), this.name, this.visibilityPredicate(), (Float)this.min, (Float)this.max);
    }
    
    @Override
    public FloatSettingBuilder withMinimum(final Float minimum) {
        return (FloatSettingBuilder)super.withMinimum(minimum);
    }
    
    @Override
    public FloatSettingBuilder withName(final String name) {
        return (FloatSettingBuilder)super.withName(name);
    }
    
    @Override
    public FloatSettingBuilder withListener(final BiConsumer<Float, Float> consumer) {
        return (FloatSettingBuilder)super.withListener(consumer);
    }
    
    @Override
    public FloatSettingBuilder withMaximum(final Float maximum) {
        return (FloatSettingBuilder)super.withMaximum(maximum);
    }
    
    @Override
    public FloatSettingBuilder withRange(final Float minimum, final Float maximum) {
        return (FloatSettingBuilder)super.withRange(minimum, maximum);
    }
    
    public FloatSettingBuilder withConsumer(final BiConsumer<Float, Float> consumer) {
        return (FloatSettingBuilder)super.withConsumer((BiConsumer)consumer);
    }
    
    @Override
    public FloatSettingBuilder withValue(final Float value) {
        return (FloatSettingBuilder)super.withValue(value);
    }
    
    public FloatSettingBuilder withVisibility(final Predicate<Float> predicate) {
        return (FloatSettingBuilder)super.withVisibility((Predicate)predicate);
    }
    
    public FloatSettingBuilder withRestriction(final Predicate<Float> predicate) {
        return (FloatSettingBuilder)super.withRestriction((Predicate)predicate);
    }
}
