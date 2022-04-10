//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "/Users/imac/Downloads/Minecraft-Deobfuscator3000-master/1.12 stable mappings"!

//Decompiled by Procyon!

package me.zeroeightsix.kami.module.modules.gui;

import me.zeroeightsix.kami.module.*;
import me.zeroeightsix.kami.setting.*;
import net.minecraft.util.text.*;
import java.util.*;
import me.zeroeightsix.kami.util.*;
import net.minecraft.client.*;
import me.zeroeightsix.kami.module.modules.movement.*;
import net.minecraft.init.*;
import me.zeroeightsix.kami.command.*;
import net.minecraft.item.*;
import java.util.function.*;

@Info(name = "InfoOverlay", category = Category.GUI, description = "Configures the game information overlay", showOnArray = ShowOnArray.OFF)
public class InfoOverlay extends Module
{
    private Setting<Page> page;
    private Setting<Boolean> version;
    private Setting<Boolean> username;
    private Setting<Boolean> tps;
    private Setting<Boolean> fps;
    private Setting<Boolean> ping;
    private Setting<Boolean> durability;
    private Setting<Boolean> memory;
    private Setting<Boolean> timerSpeed;
    private Setting<Boolean> totems;
    private Setting<Boolean> endCrystals;
    private Setting<Boolean> expBottles;
    private Setting<Boolean> godApples;
    private Setting<Boolean> speed;
    private Setting<SpeedUnit> speedUnit;
    private Setting<Boolean> time;
    public Setting<TimeUtil.TimeType> timeTypeSetting;
    public Setting<TimeUtil.TimeUnit> timeUnitSetting;
    public Setting<Boolean> doLocale;
    public Setting<ColourTextFormatting.ColourCode> firstColour;
    public Setting<ColourTextFormatting.ColourCode> secondColour;
    
    public InfoOverlay() {
        this.page = this.register((Setting<Page>)Settings.enumBuilder(Page.class).withName("Page").withValue(Page.ONE).build());
        this.version = this.register(Settings.booleanBuilder("Version").withValue(true).withVisibility(v -> this.page.getValue().equals(Page.ONE)).build());
        this.username = this.register(Settings.booleanBuilder("Username").withValue(true).withVisibility(v -> this.page.getValue().equals(Page.ONE)).build());
        this.tps = this.register(Settings.booleanBuilder("TPS").withValue(true).withVisibility(v -> this.page.getValue().equals(Page.ONE)).build());
        this.fps = this.register(Settings.booleanBuilder("FPS").withValue(true).withVisibility(v -> this.page.getValue().equals(Page.ONE)).build());
        this.ping = this.register(Settings.booleanBuilder("Ping").withValue(false).withVisibility(v -> this.page.getValue().equals(Page.ONE)).build());
        this.durability = this.register(Settings.booleanBuilder("Item Damage").withValue(false).withVisibility(v -> this.page.getValue().equals(Page.ONE)).build());
        this.memory = this.register(Settings.booleanBuilder("RAM Used").withValue(false).withVisibility(v -> this.page.getValue().equals(Page.ONE)).build());
        this.timerSpeed = this.register(Settings.booleanBuilder("Timer Speed").withValue(false).withVisibility(v -> this.page.getValue().equals(Page.ONE)).build());
        this.totems = this.register(Settings.booleanBuilder("Totems").withValue(false).withVisibility(v -> this.page.getValue().equals(Page.TWO)).build());
        this.endCrystals = this.register(Settings.booleanBuilder("End Crystals").withValue(false).withVisibility(v -> this.page.getValue().equals(Page.TWO)).build());
        this.expBottles = this.register(Settings.booleanBuilder("EXP Bottles").withValue(false).withVisibility(v -> this.page.getValue().equals(Page.TWO)).build());
        this.godApples = this.register(Settings.booleanBuilder("God Apples").withValue(false).withVisibility(v -> this.page.getValue().equals(Page.TWO)).build());
        this.speed = this.register(Settings.booleanBuilder("Speed").withValue(true).withVisibility(v -> this.page.getValue().equals(Page.THREE)).build());
        this.speedUnit = this.register((Setting<SpeedUnit>)Settings.enumBuilder(SpeedUnit.class).withName("Speed Unit").withValue(SpeedUnit.KMH).withVisibility(v -> this.page.getValue().equals(Page.THREE) && this.speed.getValue()).build());
        this.time = this.register(Settings.booleanBuilder("Time").withValue(true).withVisibility(v -> this.page.getValue().equals(Page.THREE)).build());
        this.timeTypeSetting = this.register((Setting<TimeUtil.TimeType>)Settings.enumBuilder((Class<? extends Enum>)TimeUtil.TimeType.class).withName("Time Format").withValue(TimeUtil.TimeType.HHMMSS).withVisibility(v -> this.page.getValue().equals(Page.THREE) && this.time.getValue()).build());
        this.timeUnitSetting = this.register((Setting<TimeUtil.TimeUnit>)Settings.enumBuilder((Class<? extends Enum>)TimeUtil.TimeUnit.class).withName("Time Unit").withValue(TimeUtil.TimeUnit.H12).withVisibility(v -> this.page.getValue().equals(Page.THREE) && this.time.getValue()).build());
        this.doLocale = this.register(Settings.booleanBuilder("Time Show AMPM").withValue(true).withVisibility(v -> this.page.getValue().equals(Page.THREE) && this.time.getValue()).build());
        this.firstColour = this.register((Setting<ColourTextFormatting.ColourCode>)Settings.enumBuilder((Class<? extends Enum>)ColourTextFormatting.ColourCode.class).withName("First Colour").withValue(ColourTextFormatting.ColourCode.WHITE).withVisibility(v -> this.page.getValue().equals(Page.THREE)).build());
        this.secondColour = this.register((Setting<ColourTextFormatting.ColourCode>)Settings.enumBuilder((Class<? extends Enum>)ColourTextFormatting.ColourCode.class).withName("Second Colour").withValue(ColourTextFormatting.ColourCode.RED).withVisibility(v -> this.page.getValue().equals(Page.THREE)).build());
    }
    
    public static String getStringColour(final TextFormatting c) {
        return c.toString();
    }
    
    private TextFormatting setToText(final ColourTextFormatting.ColourCode colourCode) {
        return ColourTextFormatting.toTextMap.get(colourCode);
    }
    
    public ArrayList<String> infoContents() {
        final ArrayList<String> infoContents = new ArrayList<String>();
        if (this.version.getValue()) {
            infoContents.add(getStringColour(this.setToText(this.firstColour.getValue())) + "Version" + getStringColour(this.setToText(this.secondColour.getValue())) + " " + "0.5beta");
        }
        if (this.username.getValue()) {
            infoContents.add(getStringColour(this.setToText(this.firstColour.getValue())) + "Welcome" + getStringColour(this.setToText(this.secondColour.getValue())) + " " + InfoOverlay.mc.getSession().getUsername() + "!");
        }
        if (this.time.getValue()) {
            infoContents.add(getStringColour(this.setToText(this.firstColour.getValue())) + TimeUtil.getFinalTime(this.setToText(this.secondColour.getValue()), this.setToText(this.firstColour.getValue()), (TimeUtil.TimeUnit)this.timeUnitSetting.getValue(), (TimeUtil.TimeType)this.timeTypeSetting.getValue(), Boolean.valueOf(this.doLocale.getValue())));
        }
        if (this.tps.getValue()) {
            infoContents.add(getStringColour(this.setToText(this.firstColour.getValue())) + InfoCalculator.tps() + getStringColour(this.setToText(this.secondColour.getValue())) + " tps");
        }
        if (this.fps.getValue()) {
            infoContents.add(getStringColour(this.setToText(this.firstColour.getValue())) + Minecraft.debugFPS + getStringColour(this.setToText(this.secondColour.getValue())) + " fps");
        }
        if (this.speed.getValue()) {
            infoContents.add(getStringColour(this.setToText(this.firstColour.getValue())) + InfoCalculator.speed(this.useUnitKmH(), InfoOverlay.mc) + getStringColour(this.setToText(this.secondColour.getValue())) + " " + this.unitType(this.speedUnit.getValue()));
        }
        if (this.timerSpeed.getValue()) {
            infoContents.add(getStringColour(this.setToText(this.firstColour.getValue())) + TimerSpeed.returnGui() + getStringColour(this.setToText(this.secondColour.getValue())) + "t");
        }
        if (this.ping.getValue()) {
            infoContents.add(getStringColour(this.setToText(this.firstColour.getValue())) + InfoCalculator.ping(InfoOverlay.mc) + getStringColour(this.setToText(this.secondColour.getValue())) + " ms");
        }
        if (this.durability.getValue()) {
            infoContents.add(getStringColour(this.setToText(this.firstColour.getValue())) + InfoCalculator.dura(InfoOverlay.mc) + getStringColour(this.setToText(this.secondColour.getValue())) + " dura");
        }
        if (this.memory.getValue()) {
            infoContents.add(getStringColour(this.setToText(this.firstColour.getValue())) + InfoCalculator.memory() + getStringColour(this.setToText(this.secondColour.getValue())) + "mB free");
        }
        if (this.totems.getValue()) {
            infoContents.add(getStringColour(this.setToText(this.firstColour.getValue())) + getItems(Items.TOTEM_OF_UNDYING) + getStringColour(this.setToText(this.secondColour.getValue())) + " Totems");
        }
        if (this.endCrystals.getValue()) {
            infoContents.add(getStringColour(this.setToText(this.firstColour.getValue())) + getItems(Items.END_CRYSTAL) + getStringColour(this.setToText(this.secondColour.getValue())) + " Crystals");
        }
        if (this.expBottles.getValue()) {
            infoContents.add(getStringColour(this.setToText(this.firstColour.getValue())) + getItems(Items.EXPERIENCE_BOTTLE) + getStringColour(this.setToText(this.secondColour.getValue())) + " EXP Bottles");
        }
        if (this.godApples.getValue()) {
            infoContents.add(getStringColour(this.setToText(this.firstColour.getValue())) + getItems(Items.GOLDEN_APPLE) + getStringColour(this.setToText(this.secondColour.getValue())) + " God Apples");
        }
        return infoContents;
    }
    
    public void onDisable() {
        Command.sendDisableMessage(this.getName());
    }
    
    public boolean useUnitKmH() {
        return this.speedUnit.getValue().equals(SpeedUnit.KMH);
    }
    
    public static int getItems(final Item i) {
        return InfoOverlay.mc.player.inventory.mainInventory.stream().filter(itemStack -> itemStack.getItem() == i).mapToInt(ItemStack::getCount).sum() + InfoOverlay.mc.player.inventory.offHandInventory.stream().filter(itemStack -> itemStack.getItem() == i).mapToInt(ItemStack::getCount).sum();
    }
    
    private String unitType(final SpeedUnit s) {
        switch (s) {
            case MPS: {
                return "m/s";
            }
            case KMH: {
                return "km/h";
            }
            default: {
                return "Invalid unit type (mps or kmh)";
            }
        }
    }
    
    private enum SpeedUnit
    {
        MPS, 
        KMH;
    }
    
    private enum Page
    {
        ONE, 
        TWO, 
        THREE;
    }
}
