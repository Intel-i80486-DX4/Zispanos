//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "/Users/imac/Downloads/Minecraft-Deobfuscator3000-master/1.12 stable mappings"!

//Decompiled by Procyon!

package javassist.tools.rmi;

public class Sample
{
    private ObjectImporter importer;
    private int objectId;
    
    public Object forward(final Object[] args, final int identifier) {
        return this.importer.call(this.objectId, identifier, args);
    }
    
    public static Object forwardStatic(final Object[] args, final int identifier) throws RemoteException {
        throw new RemoteException("cannot call a static method.");
    }
}
