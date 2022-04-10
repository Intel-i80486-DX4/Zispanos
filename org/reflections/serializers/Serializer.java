//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "/Users/imac/Downloads/Minecraft-Deobfuscator3000-master/1.12 stable mappings"!

//Decompiled by Procyon!

package org.reflections.serializers;

import org.reflections.*;
import java.io.*;

public interface Serializer
{
    Reflections read(final InputStream p0);
    
    File save(final Reflections p0, final String p1);
    
    String toString(final Reflections p0);
}
