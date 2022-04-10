//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "/Users/imac/Downloads/Minecraft-Deobfuscator3000-master/1.12 stable mappings"!

//Decompiled by Procyon!

package me.zeroeightsix.kami.setting.impl.numerical;

import java.util.function.*;
import me.zeroeightsix.kami.setting.converter.*;
import com.google.common.base.*;

public class FloatSetting extends NumberSetting<Float>
{
    private static final BoxedFloatConverter converter;
    
    public FloatSetting(final Float value, final Predicate<Float> restriction, final BiConsumer<Float, Float> consumer, final String name, final Predicate<Float> visibilityPredicate, final Float min, final Float max) {
        super(value, restriction, consumer, name, visibilityPredicate, min, max);
    }
    
    @Override
    public AbstractBoxedNumberConverter converter() {
        return FloatSetting.converter;
    }
    
    static {
        converter = new BoxedFloatConverter();
    }
}
