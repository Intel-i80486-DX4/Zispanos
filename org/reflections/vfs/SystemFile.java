//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "/Users/imac/Downloads/Minecraft-Deobfuscator3000-master/1.12 stable mappings"!

//Decompiled by Procyon!

package org.reflections.vfs;

import java.io.*;

public class SystemFile implements Vfs.File
{
    private final SystemDir root;
    private final File file;
    
    public SystemFile(final SystemDir root, final File file) {
        this.root = root;
        this.file = file;
    }
    
    public String getName() {
        return this.file.getName();
    }
    
    public String getRelativePath() {
        final String filepath = this.file.getPath().replace("\\", "/");
        if (filepath.startsWith(this.root.getPath())) {
            return filepath.substring(this.root.getPath().length() + 1);
        }
        return null;
    }
    
    public InputStream openInputStream() {
        try {
            return new FileInputStream(this.file);
        }
        catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
    
    @Override
    public String toString() {
        return this.file.toString();
    }
}
