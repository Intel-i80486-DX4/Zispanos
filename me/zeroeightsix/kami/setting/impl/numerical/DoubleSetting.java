//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "/Users/imac/Downloads/Minecraft-Deobfuscator3000-master/1.12 stable mappings"!

//Decompiled by Procyon!

package me.zeroeightsix.kami.setting.impl.numerical;

import java.util.function.*;
import me.zeroeightsix.kami.setting.converter.*;
import com.google.common.base.*;

public class DoubleSetting extends NumberSetting<Double>
{
    private static final BoxedDoubleConverter converter;
    
    public DoubleSetting(final Double value, final Predicate<Double> restriction, final BiConsumer<Double, Double> consumer, final String name, final Predicate<Double> visibilityPredicate, final Double min, final Double max) {
        super(value, restriction, consumer, name, visibilityPredicate, min, max);
    }
    
    @Override
    public AbstractBoxedNumberConverter converter() {
        return DoubleSetting.converter;
    }
    
    static {
        converter = new BoxedDoubleConverter();
    }
}
