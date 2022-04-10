//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "/Users/imac/Downloads/Minecraft-Deobfuscator3000-master/1.12 stable mappings"!

//Decompiled by Procyon!

package me.zeroeightsix.kami.setting;

import me.zeroeightsix.kami.setting.builder.primitive.*;
import me.zeroeightsix.kami.setting.builder.numerical.*;
import com.google.common.base.*;
import java.util.function.*;
import me.zeroeightsix.kami.setting.builder.*;

public class Settings
{
    public static FloatSettingBuilder floatBuilder() {
        return new FloatSettingBuilder();
    }
    
    public static DoubleSettingBuilder doubleBuilder() {
        return new DoubleSettingBuilder();
    }
    
    public static IntegerSettingBuilder integerBuilder() {
        return new IntegerSettingBuilder();
    }
    
    public static BooleanSettingBuilder booleanBuilder() {
        return new BooleanSettingBuilder();
    }
    
    public static StringSettingBuilder stringBuilder() {
        return new StringSettingBuilder();
    }
    
    public static EnumSettingBuilder enumBuilder(final Class<? extends Enum> clazz) {
        return new EnumSettingBuilder(clazz);
    }
    
    public static Setting<Float> f(final String name, final float value) {
        return (Setting<Float>)floatBuilder(name).withValue(value).build();
    }
    
    public static Setting<Double> d(final String name, final double value) {
        return (Setting<Double>)doubleBuilder(name).withValue(value).build();
    }
    
    public static Setting<Integer> i(final String name, final int value) {
        return (Setting<Integer>)integerBuilder(name).withValue(value).build();
    }
    
    public static Setting<Boolean> b(final String name, final boolean value) {
        return booleanBuilder(name).withValue(value).build();
    }
    
    public static Setting<Boolean> b(final String name) {
        return booleanBuilder(name).withValue(true).build();
    }
    
    public static Setting<String> s(final String name, final String value) {
        return stringBuilder(name).withValue(value).build();
    }
    
    public static <T extends Enum> Setting<T> e(final String name, final Enum value) {
        return (Setting<T>)enumBuilder(value.getClass()).withName(name).withValue(value).build();
    }
    
    public static NumericalSettingBuilder<Float> floatBuilder(final String name) {
        return new FloatSettingBuilder().withName(name);
    }
    
    public static NumericalSettingBuilder<Double> doubleBuilder(final String name) {
        return new DoubleSettingBuilder().withName(name);
    }
    
    public static NumericalSettingBuilder<Integer> integerBuilder(final String name) {
        return (NumericalSettingBuilder<Integer>)new IntegerSettingBuilder().withName(name);
    }
    
    public static BooleanSettingBuilder booleanBuilder(final String name) {
        return new BooleanSettingBuilder().withName(name);
    }
    
    public static StringSettingBuilder stringBuilder(final String name) {
        return (StringSettingBuilder)new StringSettingBuilder().withName(name);
    }
    
    public static <T> SettingBuilder<T> custom(final String name, final T initialValue, final Converter converter, final Predicate<T> restriction, final BiConsumer<T, T> consumer, final Predicate<T> visibilityPredicate) {
        return new SettingBuilder<T>() {
            @Override
            public Setting<T> build() {
                return new Setting<T>(this.initialValue, this.predicate(), this.consumer, this.name, this.visibilityPredicate()) {
                    public Converter converter() {
                        return converter;
                    }
                };
            }
        }.withName(name).withValue(initialValue).withConsumer(consumer).withVisibility(visibilityPredicate).withRestriction(restriction);
    }
    
    public static <T> SettingBuilder<T> custom(final String name, final T initialValue, final Converter converter, final Predicate<T> restriction, final BiConsumer<T, T> consumer, final boolean hidden) {
        return custom(name, initialValue, converter, restriction, consumer, t -> !hidden);
    }
    
    public static <T> SettingBuilder<T> custom(final String name, final T initialValue, final Converter converter, final Predicate<T> restriction, final boolean hidden) {
        return custom(name, initialValue, converter, restriction, (t, t2) -> {}, hidden);
    }
    
    public static <T> SettingBuilder<T> custom(final String name, final T initialValue, final Converter converter, final boolean hidden) {
        return custom(name, initialValue, converter, input -> true, (t, t2) -> {}, hidden);
    }
    
    public static <T> SettingBuilder<T> custom(final String name, final T initialValue, final Converter converter) {
        return custom(name, initialValue, converter, input -> true, (t, t2) -> {}, false);
    }
}
