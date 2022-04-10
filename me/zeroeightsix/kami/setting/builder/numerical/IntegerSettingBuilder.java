//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "/Users/imac/Downloads/Minecraft-Deobfuscator3000-master/1.12 stable mappings"!

//Decompiled by Procyon!

package me.zeroeightsix.kami.setting.builder.numerical;

import me.zeroeightsix.kami.setting.impl.numerical.*;
import java.util.function.*;
import me.zeroeightsix.kami.setting.*;
import me.zeroeightsix.kami.setting.builder.*;

public class IntegerSettingBuilder extends NumericalSettingBuilder<Integer>
{
    @Override
    public NumberSetting build() {
        return (NumberSetting)new IntegerSetting((Integer)this.initialValue, this.predicate(), this.consumer(), this.name, this.visibilityPredicate(), (Integer)this.min, (Integer)this.max);
    }
    
    @Override
    public IntegerSettingBuilder withMinimum(final Integer minimum) {
        return (IntegerSettingBuilder)super.withMinimum(minimum);
    }
    
    @Override
    public NumericalSettingBuilder withName(final String name) {
        return super.withName(name);
    }
    
    @Override
    public IntegerSettingBuilder withListener(final BiConsumer<Integer, Integer> consumer) {
        return (IntegerSettingBuilder)super.withListener(consumer);
    }
    
    @Override
    public IntegerSettingBuilder withMaximum(final Integer maximum) {
        return (IntegerSettingBuilder)super.withMaximum(maximum);
    }
    
    @Override
    public IntegerSettingBuilder withRange(final Integer minimum, final Integer maximum) {
        return (IntegerSettingBuilder)super.withRange(minimum, maximum);
    }
    
    @Override
    public IntegerSettingBuilder withValue(final Integer value) {
        return (IntegerSettingBuilder)super.withValue(value);
    }
    
    public IntegerSettingBuilder withConsumer(final BiConsumer<Integer, Integer> consumer) {
        return (IntegerSettingBuilder)super.withConsumer((BiConsumer)consumer);
    }
    
    public IntegerSettingBuilder withRestriction(final Predicate<Integer> predicate) {
        return (IntegerSettingBuilder)super.withRestriction((Predicate)predicate);
    }
    
    public IntegerSettingBuilder withVisibility(final Predicate<Integer> predicate) {
        return (IntegerSettingBuilder)super.withVisibility((Predicate)predicate);
    }
}
