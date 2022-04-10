//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "/Users/imac/Downloads/Minecraft-Deobfuscator3000-master/1.12 stable mappings"!

//Decompiled by Procyon!

package me.zeroeightsix.kami.setting.impl.numerical;

import java.util.function.*;
import me.zeroeightsix.kami.setting.converter.*;
import com.google.common.base.*;

public class IntegerSetting extends NumberSetting<Integer>
{
    private static final BoxedIntegerConverter converter;
    
    public IntegerSetting(final Integer value, final Predicate<Integer> restriction, final BiConsumer<Integer, Integer> consumer, final String name, final Predicate<Integer> visibilityPredicate, final Integer min, final Integer max) {
        super(value, restriction, consumer, name, visibilityPredicate, min, max);
    }
    
    @Override
    public AbstractBoxedNumberConverter converter() {
        return IntegerSetting.converter;
    }
    
    static {
        converter = new BoxedIntegerConverter();
    }
}
