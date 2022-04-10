//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "/Users/imac/Downloads/Minecraft-Deobfuscator3000-master/1.12 stable mappings"!

//Decompiled by Procyon!

package me.zeroeightsix.kami.module.modules.misc;

import me.zeroeightsix.kami.module.*;
import me.zeroeightsix.kami.setting.*;
import net.minecraft.tileentity.*;
import net.minecraft.util.math.*;
import me.zeroeightsix.kami.command.*;
import net.minecraft.init.*;
import net.minecraft.client.audio.*;
import java.text.*;
import java.io.*;
import java.util.*;

@Info(name = "StashFinder", category = Category.MISC, description = "Logs chests and shulker boxes to a file")
public class StashFinder extends Module
{
    private ArrayList chestPositions;
    private Map stashMap;
    private Setting density;
    private Setting logToFile;
    private Setting logShulkers;
    private Setting playSound;
    
    public StashFinder() {
        this.chestPositions = new ArrayList();
        this.stashMap = new HashMap();
        this.density = this.register((Setting<Object>)Settings.integerBuilder("Chest Density").withMinimum(2).withValue(5).withMaximum(10).build());
        this.logToFile = this.register(Settings.b("Log to file", true));
        this.logShulkers = this.register(Settings.b("Log shulkers", true));
        this.playSound = this.register(Settings.b("Play sound", true));
    }
    
    public void onEnable() {
        this.chestPositions.clear();
        this.stashMap.clear();
    }
    
    @Override
    public void onUpdate() {
        for (final TileEntity tileEntity : StashFinder.mc.world.loadedTileEntityList) {
            final BlockPos pos = tileEntity.getPos();
            if (tileEntity instanceof TileEntityChest || tileEntity instanceof TileEntityShulkerBox) {
                boolean alreadyAdded = false;
                for (final BlockPos p : this.chestPositions) {
                    if (p.equals((Object)pos)) {
                        alreadyAdded = true;
                    }
                }
                if (alreadyAdded) {
                    continue;
                }
                this.chestPositions.add(pos);
                final int chunkX = pos.getX() / 16;
                final int chunkZ = pos.getZ() / 16;
                final long chunk = ChunkPos.asLong(chunkX, chunkZ);
                if (!this.stashMap.containsKey(chunk)) {
                    this.stashMap.put(chunk, 0);
                }
                final int DENSITY = this.density.getValue();
                int count = this.stashMap.get(chunk) + 1;
                if (this.logShulkers.getValue() && tileEntity instanceof TileEntityShulkerBox && count < DENSITY) {
                    count = DENSITY;
                }
                this.stashMap.put(chunk, count);
                if (count != DENSITY) {
                    continue;
                }
                Command.sendChatMessage("[StashFinder] " + pos.toString());
                if (this.playSound.getValue()) {
                    StashFinder.mc.getSoundHandler().playSound((ISound)PositionedSoundRecord.getRecord(SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0f, 1.0f));
                }
                if (!this.logToFile.getValue()) {
                    continue;
                }
                try {
                    final BufferedWriter writer = new BufferedWriter(new FileWriter("ZispanosBackdoor-StashFinder.txt", true));
                    String line = "";
                    final Calendar calendar = Calendar.getInstance();
                    final SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    line = line + "[" + formatter.format(calendar.getTime()) + "|";
                    if (StashFinder.mc.getCurrentServerData() != null) {
                        line = line + StashFinder.mc.getCurrentServerData().serverIP + "|";
                    }
                    switch (StashFinder.mc.player.dimension) {
                        case -1: {
                            line += "Nether";
                            break;
                        }
                        case 0: {
                            line += "Overworld";
                            break;
                        }
                        case 1: {
                            line += "End";
                            break;
                        }
                    }
                    line += "] ";
                    line = line + pos.toString() + " ";
                    if (tileEntity instanceof TileEntityShulkerBox) {
                        line += "Shulker";
                    }
                    writer.write(line);
                    writer.newLine();
                    writer.close();
                }
                catch (IOException var3) {
                    var3.printStackTrace();
                }
            }
        }
    }
}
