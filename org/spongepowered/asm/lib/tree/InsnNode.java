//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "/Users/imac/Downloads/Minecraft-Deobfuscator3000-master/1.12 stable mappings"!

//Decompiled by Procyon!

package org.spongepowered.asm.lib.tree;

import org.spongepowered.asm.lib.*;
import java.util.*;

public class InsnNode extends AbstractInsnNode
{
    public InsnNode(final int opcode) {
        super(opcode);
    }
    
    @Override
    public int getType() {
        return 0;
    }
    
    @Override
    public void accept(final MethodVisitor mv) {
        mv.visitInsn(this.opcode);
        this.acceptAnnotations(mv);
    }
    
    @Override
    public AbstractInsnNode clone(final Map<LabelNode, LabelNode> labels) {
        return new InsnNode(this.opcode).cloneAnnotations(this);
    }
}
