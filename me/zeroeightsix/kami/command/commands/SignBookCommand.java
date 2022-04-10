//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "/Users/imac/Downloads/Minecraft-Deobfuscator3000-master/1.12 stable mappings"!

//Decompiled by Procyon!

package me.zeroeightsix.kami.command.commands;

import me.zeroeightsix.kami.command.*;
import me.zeroeightsix.kami.command.syntax.*;
import me.zeroeightsix.kami.util.*;
import java.util.*;
import io.netty.buffer.*;
import net.minecraft.network.play.client.*;
import net.minecraft.network.*;
import net.minecraft.item.*;
import net.minecraft.nbt.*;

public class SignBookCommand extends Command
{
    public SignBookCommand() {
        super("signbook", new ChunkBuilder().append("name").build(), new String[] { "sign" });
        this.setDescription("Colored book names. &f#n&7 for a new line and &f&&7 for colour codes");
    }
    
    public void call(final String[] args) {
        final ItemStack is = Wrapper.getPlayer().inventory.getCurrentItem();
        final int c = 167;
        if (args.length == 1) {
            Command.sendChatMessage("Please specify a title.");
            return;
        }
        if (is.getItem() instanceof ItemWritableBook) {
            final ArrayList<String> toAdd = new ArrayList<String>();
            for (int i = 0; i < args.length; ++i) {
                toAdd.add(args[i]);
            }
            String futureTitle = String.join(" ", toAdd);
            futureTitle = futureTitle.replaceAll("&", Character.toString((char)c));
            futureTitle = futureTitle.replaceAll("#n", "\n");
            futureTitle = futureTitle.replaceAll("null", "");
            if (futureTitle.length() > 31) {
                Command.sendChatMessage("Title cannot be over 31 characters.");
                return;
            }
            final NBTTagList pages = new NBTTagList();
            final String pageText = "";
            pages.appendTag((NBTBase)new NBTTagString(pageText));
            final NBTTagCompound bookData = is.getTagCompound();
            if (is.hasTagCompound()) {
                if (bookData != null) {
                    is.setTagCompound(bookData);
                }
                is.getTagCompound().setTag("title", (NBTBase)new NBTTagString(futureTitle));
                is.getTagCompound().setTag("author", (NBTBase)new NBTTagString(Wrapper.getPlayer().getName()));
            }
            else {
                is.setTagInfo("pages", (NBTBase)pages);
                is.setTagInfo("title", (NBTBase)new NBTTagString(futureTitle));
                is.setTagInfo("author", (NBTBase)new NBTTagString(Wrapper.getPlayer().getName()));
            }
            final PacketBuffer buf = new PacketBuffer(Unpooled.buffer());
            buf.writeItemStack(is);
            Wrapper.getPlayer().connection.sendPacket((Packet)new CPacketCustomPayload("MC|BSign", buf));
            Command.sendChatMessage("Signed book with title: " + futureTitle + "&r");
        }
        else {
            Command.sendChatMessage("You must be holding a writable book.");
        }
    }
}
