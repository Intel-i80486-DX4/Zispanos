//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "/Users/imac/Downloads/Minecraft-Deobfuscator3000-master/1.12 stable mappings"!

//Decompiled by Procyon!

package org.spongepowered.asm.mixin.gen;

import org.spongepowered.asm.lib.tree.*;

public class AccessorGeneratorFieldSetter extends AccessorGeneratorField
{
    public AccessorGeneratorFieldSetter(final AccessorInfo info) {
        super(info);
    }
    
    public MethodNode generate() {
        final int stackSpace = this.isInstanceField ? 1 : 0;
        final int maxLocals = stackSpace + this.targetType.getSize();
        final int maxStack = stackSpace + this.targetType.getSize();
        final MethodNode method = this.createMethod(maxLocals, maxStack);
        if (this.isInstanceField) {
            method.instructions.add(new VarInsnNode(25, 0));
        }
        method.instructions.add(new VarInsnNode(this.targetType.getOpcode(21), stackSpace));
        final int opcode = this.isInstanceField ? 181 : 179;
        method.instructions.add(new FieldInsnNode(opcode, this.info.getClassNode().name, this.targetField.name, this.targetField.desc));
        method.instructions.add(new InsnNode(177));
        return method;
    }
}
