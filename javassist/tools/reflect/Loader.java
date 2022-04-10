//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "/Users/imac/Downloads/Minecraft-Deobfuscator3000-master/1.12 stable mappings"!

//Decompiled by Procyon!

package javassist.tools.reflect;

import javassist.*;

public class Loader extends javassist.Loader
{
    protected Reflection reflection;
    
    public static void main(final String[] args) throws Throwable {
        final Loader cl = new Loader();
        cl.run(args);
    }
    
    public Loader() throws CannotCompileException, NotFoundException {
        this.delegateLoadingOf("javassist.tools.reflect.Loader");
        this.reflection = new Reflection();
        final ClassPool pool = ClassPool.getDefault();
        this.addTranslator(pool, this.reflection);
    }
    
    public boolean makeReflective(final String clazz, final String metaobject, final String metaclass) throws CannotCompileException, NotFoundException {
        return this.reflection.makeReflective(clazz, metaobject, metaclass);
    }
}
