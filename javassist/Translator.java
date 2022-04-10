//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "/Users/imac/Downloads/Minecraft-Deobfuscator3000-master/1.12 stable mappings"!

//Decompiled by Procyon!

package javassist;

public interface Translator
{
    void start(final ClassPool p0) throws NotFoundException, CannotCompileException;
    
    void onLoad(final ClassPool p0, final String p1) throws NotFoundException, CannotCompileException;
}
