//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "/Users/imac/Downloads/Minecraft-Deobfuscator3000-master/1.12 stable mappings"!

//Decompiled by Procyon!

package org.spongepowered.asm.obfuscation.mapping.common;

import org.spongepowered.asm.obfuscation.mapping.*;
import com.google.common.base.*;

public class MappingField implements IMapping<MappingField>
{
    private final String owner;
    private final String name;
    private final String desc;
    
    public MappingField(final String owner, final String name) {
        this(owner, name, null);
    }
    
    public MappingField(final String owner, final String name, final String desc) {
        this.owner = owner;
        this.name = name;
        this.desc = desc;
    }
    
    public IMapping.Type getType() {
        return IMapping.Type.FIELD;
    }
    
    public String getName() {
        return this.name;
    }
    
    public final String getSimpleName() {
        return this.name;
    }
    
    public final String getOwner() {
        return this.owner;
    }
    
    public final String getDesc() {
        return this.desc;
    }
    
    public MappingField getSuper() {
        return null;
    }
    
    public MappingField move(final String newOwner) {
        return new MappingField(newOwner, this.getName(), this.getDesc());
    }
    
    public MappingField remap(final String newName) {
        return new MappingField(this.getOwner(), newName, this.getDesc());
    }
    
    public MappingField transform(final String newDesc) {
        return new MappingField(this.getOwner(), this.getName(), newDesc);
    }
    
    public MappingField copy() {
        return new MappingField(this.getOwner(), this.getName(), this.getDesc());
    }
    
    @Override
    public int hashCode() {
        return Objects.hashCode(new Object[] { this.toString() });
    }
    
    @Override
    public boolean equals(final Object obj) {
        return this == obj || (obj instanceof MappingField && Objects.equal((Object)this.toString(), (Object)((MappingField)obj).toString()));
    }
    
    public String serialise() {
        return this.toString();
    }
    
    @Override
    public String toString() {
        return String.format("L%s;%s:%s", this.getOwner(), this.getName(), Strings.nullToEmpty(this.getDesc()));
    }
}
