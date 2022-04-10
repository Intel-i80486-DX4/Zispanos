//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "/Users/imac/Downloads/Minecraft-Deobfuscator3000-master/1.12 stable mappings"!

//Decompiled by Procyon!

package org.spongepowered.tools.obfuscation;

import org.spongepowered.tools.obfuscation.interfaces.*;
import org.spongepowered.tools.obfuscation.mapping.*;
import org.spongepowered.tools.obfuscation.service.*;
import java.util.*;

public class ObfuscationManager implements IObfuscationManager
{
    private final IMixinAnnotationProcessor ap;
    private final List<ObfuscationEnvironment> environments;
    private final IObfuscationDataProvider obfs;
    private final IReferenceManager refs;
    private final List<IMappingConsumer> consumers;
    private boolean initDone;
    
    public ObfuscationManager(final IMixinAnnotationProcessor ap) {
        this.environments = new ArrayList<ObfuscationEnvironment>();
        this.consumers = new ArrayList<IMappingConsumer>();
        this.ap = ap;
        this.obfs = new ObfuscationDataProvider(ap, this.environments);
        this.refs = (IReferenceManager)new ReferenceManager(ap, (List)this.environments);
    }
    
    @Override
    public void init() {
        if (this.initDone) {
            return;
        }
        this.initDone = true;
        ObfuscationServices.getInstance().initProviders(this.ap);
        for (final ObfuscationType obfType : ObfuscationType.types()) {
            if (obfType.isSupported()) {
                this.environments.add(obfType.createEnvironment());
            }
        }
    }
    
    @Override
    public IObfuscationDataProvider getDataProvider() {
        return this.obfs;
    }
    
    @Override
    public IReferenceManager getReferenceManager() {
        return this.refs;
    }
    
    @Override
    public IMappingConsumer createMappingConsumer() {
        final Mappings mappings = new Mappings();
        this.consumers.add((IMappingConsumer)mappings);
        return (IMappingConsumer)mappings;
    }
    
    @Override
    public List<ObfuscationEnvironment> getEnvironments() {
        return this.environments;
    }
    
    @Override
    public void writeMappings() {
        for (final ObfuscationEnvironment env : this.environments) {
            env.writeMappings(this.consumers);
        }
    }
    
    @Override
    public void writeReferences() {
        this.refs.write();
    }
}
