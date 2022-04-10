//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "/Users/imac/Downloads/Minecraft-Deobfuscator3000-master/1.12 stable mappings"!

//Decompiled by Procyon!

package org.yaml.snakeyaml.util;

public class PlatformFeatureDetector
{
    private Boolean isRunningOnAndroid;
    
    public PlatformFeatureDetector() {
        this.isRunningOnAndroid = null;
    }
    
    public boolean isRunningOnAndroid() {
        if (this.isRunningOnAndroid == null) {
            final String name = System.getProperty("java.runtime.name");
            this.isRunningOnAndroid = (name != null && name.startsWith("Android Runtime"));
        }
        return this.isRunningOnAndroid;
    }
}
