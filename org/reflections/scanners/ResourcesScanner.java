//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "/Users/imac/Downloads/Minecraft-Deobfuscator3000-master/1.12 stable mappings"!

//Decompiled by Procyon!

package org.reflections.scanners;

import org.reflections.vfs.*;
import org.reflections.*;

public class ResourcesScanner extends AbstractScanner
{
    @Override
    public boolean acceptsInput(final String file) {
        return !file.endsWith(".class");
    }
    
    @Override
    public Object scan(final Vfs.File file, final Object classObject, final Store store) {
        this.put(store, file.getName(), file.getRelativePath());
        return classObject;
    }
    
    @Override
    public void scan(final Object cls, final Store store) {
        throw new UnsupportedOperationException();
    }
}
