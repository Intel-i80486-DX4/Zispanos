//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "/Users/imac/Downloads/Minecraft-Deobfuscator3000-master/1.12 stable mappings"!

//Decompiled by Procyon!

package org.reflections.scanners;

import java.util.function.*;
import org.reflections.vfs.*;
import org.reflections.*;

public interface Scanner
{
    void setConfiguration(final Configuration p0);
    
    Scanner filterResultsBy(final Predicate<String> p0);
    
    boolean acceptsInput(final String p0);
    
    Object scan(final Vfs.File p0, final Object p1, final Store p2);
    
    boolean acceptResult(final String p0);
}
