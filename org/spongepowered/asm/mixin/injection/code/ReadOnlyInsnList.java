//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "/Users/imac/Downloads/Minecraft-Deobfuscator3000-master/1.12 stable mappings"!

//Decompiled by Procyon!

package org.spongepowered.asm.mixin.injection.code;

import org.spongepowered.asm.lib.tree.*;
import java.util.*;

class ReadOnlyInsnList extends InsnList
{
    private InsnList insnList;
    
    public ReadOnlyInsnList(final InsnList insns) {
        this.insnList = insns;
    }
    
    void dispose() {
        this.insnList = null;
    }
    
    @Override
    public final void set(final AbstractInsnNode location, final AbstractInsnNode insn) {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public final void add(final AbstractInsnNode insn) {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public final void add(final InsnList insns) {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public final void insert(final AbstractInsnNode insn) {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public final void insert(final InsnList insns) {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public final void insert(final AbstractInsnNode location, final AbstractInsnNode insn) {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public final void insert(final AbstractInsnNode location, final InsnList insns) {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public final void insertBefore(final AbstractInsnNode location, final AbstractInsnNode insn) {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public final void insertBefore(final AbstractInsnNode location, final InsnList insns) {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public final void remove(final AbstractInsnNode insn) {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public AbstractInsnNode[] toArray() {
        return this.insnList.toArray();
    }
    
    @Override
    public int size() {
        return this.insnList.size();
    }
    
    @Override
    public AbstractInsnNode getFirst() {
        return this.insnList.getFirst();
    }
    
    @Override
    public AbstractInsnNode getLast() {
        return this.insnList.getLast();
    }
    
    @Override
    public AbstractInsnNode get(final int index) {
        return this.insnList.get(index);
    }
    
    @Override
    public boolean contains(final AbstractInsnNode insn) {
        return this.insnList.contains(insn);
    }
    
    @Override
    public int indexOf(final AbstractInsnNode insn) {
        return this.insnList.indexOf(insn);
    }
    
    @Override
    public ListIterator<AbstractInsnNode> iterator() {
        return this.insnList.iterator();
    }
    
    @Override
    public ListIterator<AbstractInsnNode> iterator(final int index) {
        return this.insnList.iterator(index);
    }
    
    @Override
    public final void resetLabels() {
        this.insnList.resetLabels();
    }
}
