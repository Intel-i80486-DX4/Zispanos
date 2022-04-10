//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "/Users/imac/Downloads/Minecraft-Deobfuscator3000-master/1.12 stable mappings"!

//Decompiled by Procyon!

package org.reflections.scanners;

import java.util.function.*;
import org.reflections.vfs.*;
import org.reflections.*;
import org.reflections.util.*;
import org.reflections.adapters.*;

public abstract class AbstractScanner implements Scanner
{
    private Configuration configuration;
    private Predicate<String> resultFilter;
    
    public AbstractScanner() {
        this.resultFilter = (s -> true);
    }
    
    public boolean acceptsInput(final String file) {
        return this.getMetadataAdapter().acceptsInput(file);
    }
    
    public Object scan(final Vfs.File file, Object classObject, final Store store) {
        if (classObject == null) {
            try {
                classObject = this.configuration.getMetadataAdapter().getOrCreateClassObject(file);
            }
            catch (Exception e) {
                throw new ReflectionsException("could not create class object from file " + file.getRelativePath(), (Throwable)e);
            }
        }
        this.scan(classObject, store);
        return classObject;
    }
    
    public abstract void scan(final Object p0, final Store p1);
    
    protected void put(final Store store, final String key, final String value) {
        store.put(Utils.index((Class)this.getClass()), key, value);
    }
    
    public Configuration getConfiguration() {
        return this.configuration;
    }
    
    public void setConfiguration(final Configuration configuration) {
        this.configuration = configuration;
    }
    
    public Predicate<String> getResultFilter() {
        return this.resultFilter;
    }
    
    public void setResultFilter(final Predicate<String> resultFilter) {
        this.resultFilter = resultFilter;
    }
    
    public Scanner filterResultsBy(final Predicate<String> filter) {
        this.setResultFilter(filter);
        return (Scanner)this;
    }
    
    public boolean acceptResult(final String fqn) {
        return fqn != null && this.resultFilter.test(fqn);
    }
    
    protected MetadataAdapter getMetadataAdapter() {
        return this.configuration.getMetadataAdapter();
    }
    
    @Override
    public boolean equals(final Object o) {
        return this == o || (o != null && this.getClass() == o.getClass());
    }
    
    @Override
    public int hashCode() {
        return this.getClass().hashCode();
    }
}