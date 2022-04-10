//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "/Users/imac/Downloads/Minecraft-Deobfuscator3000-master/1.12 stable mappings"!

//Decompiled by Procyon!

package org.yaml.snakeyaml.events;

public class ImplicitTuple
{
    private final boolean plain;
    private final boolean nonPlain;
    
    public ImplicitTuple(final boolean plain, final boolean nonplain) {
        this.plain = plain;
        this.nonPlain = nonplain;
    }
    
    public boolean canOmitTagInPlainScalar() {
        return this.plain;
    }
    
    public boolean canOmitTagInNonPlainScalar() {
        return this.nonPlain;
    }
    
    public boolean bothFalse() {
        return !this.plain && !this.nonPlain;
    }
    
    @Override
    public String toString() {
        return "implicit=[" + this.plain + ", " + this.nonPlain + "]";
    }
}
