//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "/Users/imac/Downloads/Minecraft-Deobfuscator3000-master/1.12 stable mappings"!

//Decompiled by Procyon!

package org.spongepowered.tools.obfuscation.mapping.common;

import org.spongepowered.tools.obfuscation.mapping.*;
import javax.annotation.processing.*;
import org.spongepowered.asm.obfuscation.mapping.common.*;
import com.google.common.collect.*;

public abstract class MappingProvider implements IMappingProvider
{
    protected final Messager messager;
    protected final Filer filer;
    protected final BiMap<String, String> packageMap;
    protected final BiMap<String, String> classMap;
    protected final BiMap<MappingField, MappingField> fieldMap;
    protected final BiMap<MappingMethod, MappingMethod> methodMap;
    
    public MappingProvider(final Messager messager, final Filer filer) {
        this.packageMap = (BiMap<String, String>)HashBiMap.create();
        this.classMap = (BiMap<String, String>)HashBiMap.create();
        this.fieldMap = (BiMap<MappingField, MappingField>)HashBiMap.create();
        this.methodMap = (BiMap<MappingMethod, MappingMethod>)HashBiMap.create();
        this.messager = messager;
        this.filer = filer;
    }
    
    public void clear() {
        this.packageMap.clear();
        this.classMap.clear();
        this.fieldMap.clear();
        this.methodMap.clear();
    }
    
    public boolean isEmpty() {
        return this.packageMap.isEmpty() && this.classMap.isEmpty() && this.fieldMap.isEmpty() && this.methodMap.isEmpty();
    }
    
    public MappingMethod getMethodMapping(final MappingMethod method) {
        return (MappingMethod)this.methodMap.get((Object)method);
    }
    
    public MappingField getFieldMapping(final MappingField field) {
        return (MappingField)this.fieldMap.get((Object)field);
    }
    
    public String getClassMapping(final String className) {
        return (String)this.classMap.get((Object)className);
    }
    
    public String getPackageMapping(final String packageName) {
        return (String)this.packageMap.get((Object)packageName);
    }
}
