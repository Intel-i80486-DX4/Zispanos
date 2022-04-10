//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "/Users/imac/Downloads/Minecraft-Deobfuscator3000-master/1.12 stable mappings"!

//Decompiled by Procyon!

package me.zeroeightsix.kami.command.commands;

import me.zeroeightsix.kami.command.*;
import me.zeroeightsix.kami.command.syntax.*;
import me.zeroeightsix.kami.util.*;
import java.util.*;
import net.minecraft.nbt.*;
import io.netty.buffer.*;
import net.minecraft.network.play.client.*;
import net.minecraft.network.*;
import net.minecraft.item.*;
import java.util.stream.*;

public class DupeBookCommand extends Command
{
    public DupeBookCommand() {
        super("dupebook", new ChunkBuilder().append("name").build(), new String[0]);
        this.setDescription("Generates books used for chunk savestate dupe.");
    }
    
    public void call(final String[] args) {
        final ItemStack heldItem = Wrapper.getPlayer().inventory.getCurrentItem();
        if (heldItem.getItem() instanceof ItemWritableBook) {
            final IntStream characterGenerator = new Random().ints(128, 1112063).map(i -> (i < 55296) ? i : (i + 2048));
            final NBTTagList pages = new NBTTagList();
            final String joinedPages = characterGenerator.limit(10500L).mapToObj(i -> String.valueOf((char)i)).collect((Collector<? super Object, ?, String>)Collectors.joining());
            for (int page = 0; page < 50; ++page) {
                pages.appendTag((NBTBase)new NBTTagString(joinedPages.substring(page * 210, (page + 1) * 210)));
            }
            if (heldItem.hasTagCompound()) {
                assert heldItem.getTagCompound() != null;
                heldItem.getTagCompound().setTag("pages", (NBTBase)pages);
                heldItem.getTagCompound().setTag("title", (NBTBase)new NBTTagString(""));
                heldItem.getTagCompound().setTag("author", (NBTBase)new NBTTagString(Wrapper.getPlayer().getName()));
            }
            else {
                heldItem.setTagInfo("pages", (NBTBase)pages);
                heldItem.setTagInfo("title", (NBTBase)new NBTTagString(""));
                heldItem.setTagInfo("author", (NBTBase)new NBTTagString(Wrapper.getPlayer().getName()));
            }
            final PacketBuffer buf = new PacketBuffer(Unpooled.buffer());
            buf.writeItemStack(heldItem);
            Wrapper.getPlayer().connection.sendPacket((Packet)new CPacketCustomPayload("MC|BEdit", buf));
            Command.sendChatMessage("Dupe book generated.");
        }
        else {
            Command.sendErrorMessage("You must be holding a writable book.");
        }
    }
}
