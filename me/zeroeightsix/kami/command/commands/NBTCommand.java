//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "/Users/imac/Downloads/Minecraft-Deobfuscator3000-master/1.12 stable mappings"!

//Decompiled by Procyon!

package me.zeroeightsix.kami.command.commands;

import me.zeroeightsix.kami.command.*;
import net.minecraft.client.*;
import me.zeroeightsix.kami.command.syntax.parsers.*;
import me.zeroeightsix.kami.command.syntax.*;
import java.awt.*;
import java.awt.datatransfer.*;
import net.minecraft.nbt.*;
import net.minecraft.item.*;

public class NBTCommand extends Command
{
    Minecraft mc;
    private final Clipboard clipboard;
    StringSelection nbt;
    
    public NBTCommand() {
        super("nbt", new ChunkBuilder().append("action", true, new EnumParser(new String[] { "get", "copy", "wipe" })).build(), new String[0]);
        this.mc = Minecraft.getMinecraft();
        this.clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        this.setDescription("Does NBT related stuff (&fget&7, &fcopy&7, &fset&7)");
    }
    
    public void call(final String[] args) {
        if (args[0].isEmpty()) {
            sendErrorMessage("Invalid Syntax!");
            return;
        }
        final ItemStack item = this.mc.player.inventory.getCurrentItem();
        if (args[0].equalsIgnoreCase("get")) {
            if (item.getTagCompound() != null) {
                sendChatMessage("&6&lNBT:\n" + item.getTagCompound() + "");
            }
            else {
                sendErrorMessage("No NBT on " + item.getDisplayName());
            }
        }
        else if (args[0].equalsIgnoreCase("copy")) {
            if (item.getTagCompound() != null) {
                this.nbt = new StringSelection(item.getTagCompound() + "");
                this.clipboard.setContents(this.nbt, this.nbt);
                sendChatMessage("&6Copied\n&f" + item.getTagCompound() + "\n" + "&6to clipboard.");
            }
            else {
                sendErrorMessage("No NBT on " + item.getDisplayName());
            }
        }
        else if (args[0].equalsIgnoreCase("wipe")) {
            sendChatMessage("&6Wiped\n&f" + item.getTagCompound() + "\n" + "&6from " + item.getDisplayName() + ".");
            item.setTagCompound(new NBTTagCompound());
        }
    }
}
