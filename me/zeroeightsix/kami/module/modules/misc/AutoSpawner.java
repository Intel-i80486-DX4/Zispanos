//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "/Users/imac/Downloads/Minecraft-Deobfuscator3000-master/1.12 stable mappings"!

//Decompiled by Procyon!

package me.zeroeightsix.kami.module.modules.misc;

import me.zeroeightsix.kami.module.*;
import me.zeroeightsix.kami.setting.*;
import me.zeroeightsix.kami.util.*;
import net.minecraft.network.play.client.*;
import net.minecraft.entity.*;
import net.minecraft.network.*;
import net.minecraft.util.*;
import net.minecraft.entity.boss.*;
import me.zeroeightsix.kami.command.*;
import net.minecraft.entity.player.*;
import net.minecraft.item.*;
import net.minecraft.block.state.*;
import net.minecraft.init.*;
import com.mojang.realmsclient.gui.*;
import me.zeroeightsix.kami.module.modules.crystals.*;
import java.util.*;
import net.minecraft.block.*;
import net.minecraft.util.math.*;
import net.minecraft.entity.item.*;

@Info(name = "AutoSpawner", category = Category.MISC, description = "Automatically spawns Withers, Iron Golems and Snowmen")
public class AutoSpawner extends Module
{
    private Setting<UseMode> useMode;
    private Setting<Boolean> party;
    private Setting<Boolean> partyWithers;
    private Setting<EntityMode> entityMode;
    private Setting<Boolean> nametagWithers;
    private Setting<Float> placeRange;
    private Setting<Integer> delay;
    private Setting<Boolean> rotate;
    private Setting<Boolean> debug;
    private static boolean isSneaking;
    private BlockPos placeTarget;
    private boolean rotationPlaceableX;
    private boolean rotationPlaceableZ;
    private int bodySlot;
    private int headSlot;
    private int buildStage;
    private int delayStep;
    
    public AutoSpawner() {
        this.useMode = this.register(Settings.e("Use Mode", UseMode.SPAM));
        this.party = this.register(Settings.b("Party", false));
        this.partyWithers = this.register(Settings.booleanBuilder("Withers").withValue(false).withVisibility(v -> this.party.getValue()).build());
        this.entityMode = this.register((Setting<EntityMode>)Settings.enumBuilder(EntityMode.class).withName("Entity Mode").withValue(EntityMode.SNOW).withVisibility(v -> !this.party.getValue()).build());
        this.nametagWithers = this.register(Settings.booleanBuilder("Nametag").withValue(true).withVisibility(v -> (this.party.getValue() && this.partyWithers.getValue()) || (!this.party.getValue() && this.entityMode.getValue().equals(EntityMode.WITHER))).build());
        this.placeRange = this.register((Setting<Float>)Settings.floatBuilder("Place Range").withMinimum(2.0f).withValue(3.5f).withMaximum(10.0f).build());
        this.delay = this.register(Settings.integerBuilder("Delay").withMinimum(12).withValue(20).withMaximum(100).withVisibility(v -> this.useMode.getValue().equals(UseMode.SPAM)).build());
        this.rotate = this.register(Settings.b("Rotate", true));
        this.debug = this.register(Settings.b("Debug", false));
    }
    
    private static void placeBlock(final BlockPos pos, final boolean rotate) {
        final EnumFacing side = getPlaceableSide(pos);
        if (side == null) {
            return;
        }
        final BlockPos neighbour = pos.offset(side);
        final EnumFacing opposite = side.getOpposite();
        final Vec3d hitVec = new Vec3d((Vec3i)neighbour).add(0.5, 0.5, 0.5).add(new Vec3d(opposite.getDirectionVec()).scale(0.5));
        final Block neighbourBlock = AutoSpawner.mc.world.getBlockState(neighbour).getBlock();
        if (!AutoSpawner.isSneaking && (BlockInteractionHelper.blackList.contains(neighbourBlock) || BlockInteractionHelper.shulkerList.contains(neighbourBlock))) {
            AutoSpawner.mc.player.connection.sendPacket((Packet)new CPacketEntityAction((Entity)AutoSpawner.mc.player, CPacketEntityAction.Action.START_SNEAKING));
            AutoSpawner.isSneaking = true;
        }
        if (rotate) {
            BlockInteractionHelper.faceVectorPacketInstant(hitVec);
        }
        AutoSpawner.mc.playerController.processRightClickBlock(AutoSpawner.mc.player, AutoSpawner.mc.world, neighbour, opposite, hitVec, EnumHand.MAIN_HAND);
        AutoSpawner.mc.player.swingArm(EnumHand.MAIN_HAND);
        AutoSpawner.mc.rightClickDelayTimer = 4;
    }
    
    private void useNameTag() {
        final int originalSlot = AutoSpawner.mc.player.inventory.currentItem;
        for (final Entity w : AutoSpawner.mc.world.getLoadedEntityList()) {
            if (w instanceof EntityWither && w.getDisplayName().getUnformattedText().equalsIgnoreCase("Wither")) {
                final EntityWither wither = (EntityWither)w;
                if (AutoSpawner.mc.player.getDistance((Entity)wither) > this.placeRange.getValue()) {
                    continue;
                }
                if (this.debug.getValue()) {
                    Command.sendChatMessage("Found Unnamed Wither");
                }
                this.selectNameTags();
                AutoSpawner.mc.playerController.interactWithEntity((EntityPlayer)AutoSpawner.mc.player, (Entity)wither, EnumHand.MAIN_HAND);
            }
        }
        AutoSpawner.mc.player.inventory.currentItem = originalSlot;
    }
    
    private void selectNameTags() {
        int tagSlot = -1;
        for (int i = 0; i < 9; ++i) {
            final ItemStack stack = AutoSpawner.mc.player.inventory.getStackInSlot(i);
            if (stack != ItemStack.EMPTY) {
                if (!(stack.getItem() instanceof ItemBlock)) {
                    final Item tag = stack.getItem();
                    if (tag instanceof ItemNameTag) {
                        tagSlot = i;
                    }
                }
            }
        }
        if (tagSlot == -1) {
            if (this.debug.getValue()) {
                Command.sendErrorMessage(this.getChatName() + "Error: No nametags in hotbar");
            }
            return;
        }
        AutoSpawner.mc.player.inventory.currentItem = tagSlot;
    }
    
    private static EnumFacing getPlaceableSide(final BlockPos pos) {
        for (final EnumFacing side : EnumFacing.values()) {
            final BlockPos neighbour = pos.offset(side);
            if (AutoSpawner.mc.world.getBlockState(neighbour).getBlock().canCollideCheck(AutoSpawner.mc.world.getBlockState(neighbour), false)) {
                final IBlockState blockState = AutoSpawner.mc.world.getBlockState(neighbour);
                if (!blockState.getMaterial().isReplaceable() && !(blockState.getBlock() instanceof BlockTallGrass) && !(blockState.getBlock() instanceof BlockDeadBush)) {
                    return side;
                }
            }
        }
        return null;
    }
    
    @Override
    protected void onEnable() {
        if (AutoSpawner.mc.player == null) {
            this.disable();
            return;
        }
        this.buildStage = 1;
        this.delayStep = 1;
    }
    
    private boolean checkBlocksInHotbar() {
        this.headSlot = -1;
        this.bodySlot = -1;
        for (int i = 0; i < 9; ++i) {
            final ItemStack stack = AutoSpawner.mc.player.inventory.getStackInSlot(i);
            if (stack != ItemStack.EMPTY) {
                if (this.entityMode.getValue().equals(EntityMode.WITHER)) {
                    if (stack.getItem() == Items.SKULL && stack.getItemDamage() == 1) {
                        if (AutoSpawner.mc.player.inventory.getStackInSlot(i).stackSize >= 3) {
                            this.headSlot = i;
                        }
                        continue;
                    }
                    else {
                        if (!(stack.getItem() instanceof ItemBlock)) {
                            continue;
                        }
                        final Block block = ((ItemBlock)stack.getItem()).getBlock();
                        if (block instanceof BlockSoulSand && AutoSpawner.mc.player.inventory.getStackInSlot(i).stackSize >= 4) {
                            this.bodySlot = i;
                        }
                    }
                }
                if (this.entityMode.getValue().equals(EntityMode.IRON)) {
                    if (!(stack.getItem() instanceof ItemBlock)) {
                        continue;
                    }
                    final Block block = ((ItemBlock)stack.getItem()).getBlock();
                    if ((block == Blocks.LIT_PUMPKIN || block == Blocks.PUMPKIN) && AutoSpawner.mc.player.inventory.getStackInSlot(i).stackSize >= 1) {
                        this.headSlot = i;
                    }
                    if (block == Blocks.IRON_BLOCK && AutoSpawner.mc.player.inventory.getStackInSlot(i).stackSize >= 4) {
                        this.bodySlot = i;
                    }
                }
                if (this.entityMode.getValue().equals(EntityMode.SNOW)) {
                    if (stack.getItem() instanceof ItemBlock) {
                        final Block block = ((ItemBlock)stack.getItem()).getBlock();
                        if ((block == Blocks.LIT_PUMPKIN || block == Blocks.PUMPKIN) && AutoSpawner.mc.player.inventory.getStackInSlot(i).stackSize >= 1) {
                            this.headSlot = i;
                        }
                        if (block == Blocks.SNOW && AutoSpawner.mc.player.inventory.getStackInSlot(i).stackSize >= 2) {
                            this.bodySlot = i;
                        }
                    }
                }
            }
        }
        return this.bodySlot != -1 && this.headSlot != -1;
    }
    
    private boolean testStructure() {
        if (this.entityMode.getValue().equals(EntityMode.WITHER)) {
            return this.testWitherStructure();
        }
        if (this.entityMode.getValue().equals(EntityMode.IRON)) {
            return this.testIronGolemStructure();
        }
        return this.entityMode.getValue().equals(EntityMode.SNOW) && this.testSnowGolemStructure();
    }
    
    private boolean testWitherStructure() {
        boolean noRotationPlaceable = true;
        this.rotationPlaceableX = true;
        this.rotationPlaceableZ = true;
        boolean isShitGrass = false;
        if (AutoSpawner.mc.world.getBlockState(this.placeTarget) == null) {
            return false;
        }
        final Block block = AutoSpawner.mc.world.getBlockState(this.placeTarget).getBlock();
        if (block instanceof BlockTallGrass || block instanceof BlockDeadBush) {
            isShitGrass = true;
        }
        if (getPlaceableSide(this.placeTarget.up()) == null) {
            return false;
        }
        for (final BlockPos pos : BodyParts.bodyBase) {
            if (this.placingIsBlocked(this.placeTarget.add((Vec3i)pos))) {
                noRotationPlaceable = false;
            }
        }
        for (final BlockPos pos : BodyParts.ArmsX) {
            if (this.placingIsBlocked(this.placeTarget.add((Vec3i)pos)) || this.placingIsBlocked(this.placeTarget.add((Vec3i)pos.down()))) {
                this.rotationPlaceableX = false;
            }
        }
        for (final BlockPos pos : BodyParts.ArmsZ) {
            if (this.placingIsBlocked(this.placeTarget.add((Vec3i)pos)) || this.placingIsBlocked(this.placeTarget.add((Vec3i)pos.down()))) {
                this.rotationPlaceableZ = false;
            }
        }
        for (final BlockPos pos : BodyParts.headsX) {
            if (this.placingIsBlocked(this.placeTarget.add((Vec3i)pos))) {
                this.rotationPlaceableX = false;
            }
        }
        for (final BlockPos pos : BodyParts.headsZ) {
            if (this.placingIsBlocked(this.placeTarget.add((Vec3i)pos))) {
                this.rotationPlaceableZ = false;
            }
        }
        return !isShitGrass && noRotationPlaceable && (this.rotationPlaceableX || this.rotationPlaceableZ);
    }
    
    private boolean testIronGolemStructure() {
        boolean noRotationPlaceable = true;
        this.rotationPlaceableX = true;
        this.rotationPlaceableZ = true;
        boolean isShitGrass = false;
        if (AutoSpawner.mc.world.getBlockState(this.placeTarget) == null) {
            return false;
        }
        final Block block = AutoSpawner.mc.world.getBlockState(this.placeTarget).getBlock();
        if (block instanceof BlockTallGrass || block instanceof BlockDeadBush) {
            isShitGrass = true;
        }
        if (getPlaceableSide(this.placeTarget.up()) == null) {
            return false;
        }
        for (final BlockPos pos : BodyParts.bodyBase) {
            if (this.placingIsBlocked(this.placeTarget.add((Vec3i)pos))) {
                noRotationPlaceable = false;
            }
        }
        for (final BlockPos pos : BodyParts.ArmsX) {
            if (this.placingIsBlocked(this.placeTarget.add((Vec3i)pos)) || this.placingIsBlocked(this.placeTarget.add((Vec3i)pos.down()))) {
                this.rotationPlaceableX = false;
            }
        }
        for (final BlockPos pos : BodyParts.ArmsZ) {
            if (this.placingIsBlocked(this.placeTarget.add((Vec3i)pos)) || this.placingIsBlocked(this.placeTarget.add((Vec3i)pos.down()))) {
                this.rotationPlaceableZ = false;
            }
        }
        for (final BlockPos pos : BodyParts.head) {
            if (this.placingIsBlocked(this.placeTarget.add((Vec3i)pos))) {
                noRotationPlaceable = false;
            }
        }
        return !isShitGrass && noRotationPlaceable && (this.rotationPlaceableX || this.rotationPlaceableZ);
    }
    
    private boolean testSnowGolemStructure() {
        boolean noRotationPlaceable = true;
        boolean isShitGrass = false;
        if (AutoSpawner.mc.world.getBlockState(this.placeTarget) == null) {
            return false;
        }
        final Block block = AutoSpawner.mc.world.getBlockState(this.placeTarget).getBlock();
        if (block instanceof BlockTallGrass || block instanceof BlockDeadBush) {
            isShitGrass = true;
        }
        if (getPlaceableSide(this.placeTarget.up()) == null) {
            return false;
        }
        for (final BlockPos pos : BodyParts.bodyBase) {
            if (this.placingIsBlocked(this.placeTarget.add((Vec3i)pos))) {
                noRotationPlaceable = false;
            }
        }
        for (final BlockPos pos : BodyParts.head) {
            if (this.placingIsBlocked(this.placeTarget.add((Vec3i)pos))) {
                noRotationPlaceable = false;
            }
        }
        return !isShitGrass && noRotationPlaceable;
    }
    
    @Override
    public void onUpdate() {
        if (AutoSpawner.mc.player == null) {
            return;
        }
        if (this.nametagWithers.getValue() && ((this.party.getValue() && this.partyWithers.getValue()) || (!this.party.getValue() && this.entityMode.getValue().equals(EntityMode.WITHER)))) {
            this.useNameTag();
        }
        if (this.buildStage == 1) {
            AutoSpawner.isSneaking = false;
            this.rotationPlaceableX = false;
            this.rotationPlaceableZ = false;
            if (this.party.getValue()) {
                final Random random = new Random();
                int partyMode;
                if (this.partyWithers.getValue()) {
                    partyMode = random.nextInt(3);
                }
                else {
                    partyMode = random.nextInt(2);
                }
                if (partyMode == 0) {
                    this.entityMode.setValue(EntityMode.SNOW);
                }
                else if (partyMode == 1) {
                    this.entityMode.setValue(EntityMode.IRON);
                }
                else if (partyMode == 2) {
                    this.entityMode.setValue(EntityMode.WITHER);
                }
            }
            if (!this.checkBlocksInHotbar()) {
                if (!this.party.getValue()) {
                    if (this.debug.getValue()) {
                        Command.sendChatMessage(this.getChatName() + ChatFormatting.RED.toString() + "Blocks missing for: " + ChatFormatting.RESET.toString() + this.entityMode.getValue().toString() + ChatFormatting.RED.toString() + ", disabling.");
                    }
                    this.disable();
                }
                return;
            }
            final List<BlockPos> blockPosList = AutoCrystalAtom3.getSphere(AutoSpawner.mc.player.getPosition().down(), this.placeRange.getValue(), this.placeRange.getValue().intValue(), false, true, 0);
            boolean noPositionInArea = true;
            for (final BlockPos pos : blockPosList) {
                this.placeTarget = pos.down();
                if (this.testStructure()) {
                    noPositionInArea = false;
                    break;
                }
            }
            if (noPositionInArea) {
                if (this.useMode.getValue().equals(UseMode.SINGLE)) {
                    if (this.debug.getValue()) {
                        Command.sendChatMessage(this.getChatName() + ChatFormatting.RED.toString() + "Position not valid, disabling.");
                    }
                    this.disable();
                }
                return;
            }
            AutoSpawner.mc.player.inventory.currentItem = this.bodySlot;
            for (final BlockPos pos2 : BodyParts.bodyBase) {
                placeBlock(this.placeTarget.add((Vec3i)pos2), this.rotate.getValue());
            }
            if (this.entityMode.getValue().equals(EntityMode.WITHER) || this.entityMode.getValue().equals(EntityMode.IRON)) {
                if (this.rotationPlaceableX) {
                    for (final BlockPos pos2 : BodyParts.ArmsX) {
                        placeBlock(this.placeTarget.add((Vec3i)pos2), this.rotate.getValue());
                    }
                }
                else if (this.rotationPlaceableZ) {
                    for (final BlockPos pos2 : BodyParts.ArmsZ) {
                        placeBlock(this.placeTarget.add((Vec3i)pos2), this.rotate.getValue());
                    }
                }
            }
            this.buildStage = 2;
        }
        else if (this.buildStage == 2) {
            AutoSpawner.mc.player.inventory.currentItem = this.headSlot;
            if (this.entityMode.getValue().equals(EntityMode.WITHER)) {
                if (this.rotationPlaceableX) {
                    for (final BlockPos pos : BodyParts.headsX) {
                        placeBlock(this.placeTarget.add((Vec3i)pos), this.rotate.getValue());
                    }
                }
                else if (this.rotationPlaceableZ) {
                    for (final BlockPos pos : BodyParts.headsZ) {
                        placeBlock(this.placeTarget.add((Vec3i)pos), this.rotate.getValue());
                    }
                }
            }
            if (this.entityMode.getValue().equals(EntityMode.IRON) || this.entityMode.getValue().equals(EntityMode.SNOW)) {
                for (final BlockPos pos : BodyParts.head) {
                    placeBlock(this.placeTarget.add((Vec3i)pos), this.rotate.getValue());
                }
            }
            if (AutoSpawner.isSneaking) {
                AutoSpawner.mc.player.connection.sendPacket((Packet)new CPacketEntityAction((Entity)AutoSpawner.mc.player, CPacketEntityAction.Action.STOP_SNEAKING));
                AutoSpawner.isSneaking = false;
            }
            if (this.useMode.getValue().equals(UseMode.SINGLE)) {
                this.disable();
            }
            this.buildStage = 3;
        }
        else if (this.buildStage == 3) {
            if (this.delayStep < this.delay.getValue()) {
                ++this.delayStep;
            }
            else {
                this.delayStep = 1;
                this.buildStage = 1;
            }
        }
    }
    
    private boolean placingIsBlocked(final BlockPos pos) {
        final Block block = AutoSpawner.mc.world.getBlockState(pos).getBlock();
        if (!(block instanceof BlockAir)) {
            return true;
        }
        for (final Entity entity : AutoSpawner.mc.world.getEntitiesWithinAABBExcludingEntity((Entity)null, new AxisAlignedBB(pos))) {
            if (!(entity instanceof EntityItem) && !(entity instanceof EntityXPOrb)) {
                return true;
            }
        }
        return false;
    }
    
    @Override
    public String getHudInfo() {
        if (!this.party.getValue()) {
            return this.entityMode.getValue().toString();
        }
        if (this.partyWithers.getValue()) {
            return "PARTY WITHER";
        }
        return "PARTY";
    }
    
    private enum UseMode
    {
        SINGLE, 
        SPAM;
    }
    
    private enum EntityMode
    {
        SNOW, 
        IRON, 
        WITHER;
    }
    
    private static class BodyParts
    {
        private static final BlockPos[] bodyBase;
        private static final BlockPos[] ArmsX;
        private static final BlockPos[] ArmsZ;
        private static final BlockPos[] headsX;
        private static final BlockPos[] headsZ;
        private static final BlockPos[] head;
        
        static {
            bodyBase = new BlockPos[] { new BlockPos(0, 1, 0), new BlockPos(0, 2, 0) };
            ArmsX = new BlockPos[] { new BlockPos(-1, 2, 0), new BlockPos(1, 2, 0) };
            ArmsZ = new BlockPos[] { new BlockPos(0, 2, -1), new BlockPos(0, 2, 1) };
            headsX = new BlockPos[] { new BlockPos(0, 3, 0), new BlockPos(-1, 3, 0), new BlockPos(1, 3, 0) };
            headsZ = new BlockPos[] { new BlockPos(0, 3, 0), new BlockPos(0, 3, -1), new BlockPos(0, 3, 1) };
            head = new BlockPos[] { new BlockPos(0, 3, 0) };
        }
    }
}
