//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "/Users/imac/Downloads/Minecraft-Deobfuscator3000-master/1.12 stable mappings"!

//Decompiled by Procyon!

package javassist.tools.reflect;

public class Sample
{
    private Metaobject _metaobject;
    private static ClassMetaobject _classobject;
    
    public Object trap(final Object[] args, final int identifier) throws Throwable {
        final Metaobject mobj = this._metaobject;
        if (mobj == null) {
            return ClassMetaobject.invoke((Object)this, identifier, args);
        }
        return mobj.trapMethodcall(identifier, args);
    }
    
    public static Object trapStatic(final Object[] args, final int identifier) throws Throwable {
        return Sample._classobject.trapMethodcall(identifier, args);
    }
    
    public static Object trapRead(final Object[] args, final String name) {
        if (args[0] == null) {
            return Sample._classobject.trapFieldRead(name);
        }
        return ((Metalevel)args[0])._getMetaobject().trapFieldRead(name);
    }
    
    public static Object trapWrite(final Object[] args, final String name) {
        final Metalevel base = (Metalevel)args[0];
        if (base == null) {
            Sample._classobject.trapFieldWrite(name, args[1]);
        }
        else {
            base._getMetaobject().trapFieldWrite(name, args[1]);
        }
        return null;
    }
}
