//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "/Users/imac/Downloads/Minecraft-Deobfuscator3000-master/1.12 stable mappings"!

//Decompiled by Procyon!

package org.yaml.snakeyaml.constructor;

public class CustomClassLoaderConstructor extends Constructor
{
    private ClassLoader loader;
    
    public CustomClassLoaderConstructor(final ClassLoader cLoader) {
        this(Object.class, cLoader);
    }
    
    public CustomClassLoaderConstructor(final Class<?> theRoot, final ClassLoader theLoader) {
        super((Class)theRoot);
        this.loader = CustomClassLoaderConstructor.class.getClassLoader();
        if (theLoader == null) {
            throw new NullPointerException("Loader must be provided.");
        }
        this.loader = theLoader;
    }
    
    protected Class<?> getClassForName(final String name) throws ClassNotFoundException {
        return Class.forName(name, true, this.loader);
    }
}
