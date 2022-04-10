//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "/Users/imac/Downloads/Minecraft-Deobfuscator3000-master/1.12 stable mappings"!

//Decompiled by Procyon!

package javassist;

import java.io.*;
import java.net.*;

public interface ClassPath
{
    InputStream openClassfile(final String p0) throws NotFoundException;
    
    URL find(final String p0);
}
