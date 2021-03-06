//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "/Users/imac/Downloads/Minecraft-Deobfuscator3000-master/1.12 stable mappings"!

//Decompiled by Procyon!

package org.reflections;

import java.util.concurrent.*;
import org.reflections.util.*;
import java.util.*;
import java.util.function.*;
import java.util.stream.*;

public class Store
{
    private final ConcurrentHashMap<String, Map<String, Collection<String>>> storeMap;
    
    protected Store() {
        this.storeMap = new ConcurrentHashMap<String, Map<String, Collection<String>>>();
    }
    
    public Set<String> keySet() {
        return this.storeMap.keySet();
    }
    
    private Map<String, Collection<String>> get(final String index) {
        final Map<String, Collection<String>> mmap = this.storeMap.get(index);
        if (mmap == null) {
            throw new ReflectionsException("Scanner " + index + " was not configured");
        }
        return mmap;
    }
    
    public Set<String> get(final Class<?> scannerClass, final String key) {
        return this.get(Utils.index((Class)scannerClass), Collections.singletonList(key));
    }
    
    public Set<String> get(final String index, final String key) {
        return this.get(index, Collections.singletonList(key));
    }
    
    public Set<String> get(final Class<?> scannerClass, final Collection<String> keys) {
        return this.get(Utils.index((Class)scannerClass), keys);
    }
    
    private Set<String> get(final String index, final Collection<String> keys) {
        final Map<String, Collection<String>> mmap = this.get(index);
        final Set<String> result = new LinkedHashSet<String>();
        for (final String key : keys) {
            final Collection<String> values = mmap.get(key);
            if (values != null) {
                result.addAll(values);
            }
        }
        return result;
    }
    
    public Set<String> getAllIncluding(final Class<?> scannerClass, final Collection<String> keys) {
        final String index = Utils.index((Class)scannerClass);
        final Map<String, Collection<String>> mmap = this.get(index);
        final List<String> workKeys = new ArrayList<String>(keys);
        final Set<String> result = new HashSet<String>();
        for (int i = 0; i < workKeys.size(); ++i) {
            final String key = workKeys.get(i);
            if (result.add(key)) {
                final Collection<String> values = mmap.get(key);
                if (values != null) {
                    workKeys.addAll(values);
                }
            }
        }
        return result;
    }
    
    public Set<String> getAll(final Class<?> scannerClass, final String key) {
        return this.getAllIncluding(scannerClass, this.get(scannerClass, key));
    }
    
    public Set<String> getAll(final Class<?> scannerClass, final Collection<String> keys) {
        return this.getAllIncluding(scannerClass, this.get(scannerClass, keys));
    }
    
    public Set<String> keys(final String index) {
        final Map<String, Collection<String>> map = this.storeMap.get(index);
        return (map != null) ? new HashSet<String>(map.keySet()) : Collections.emptySet();
    }
    
    public Set<String> values(final String index) {
        final Map<String, Collection<String>> map = this.storeMap.get(index);
        return (Set<String>)((map != null) ? map.values().stream().flatMap((Function<? super Collection<String>, ? extends Stream<?>>)Collection::stream).collect((Collector<? super Object, ?, Set<? super Object>>)Collectors.toSet()) : Collections.emptySet());
    }
    
    public boolean put(final Class<?> scannerClass, final String key, final String value) {
        return this.put(Utils.index((Class)scannerClass), key, value);
    }
    
    public boolean put(final String index, final String key, final String value) {
        return this.storeMap.computeIfAbsent(index, s -> new ConcurrentHashMap()).computeIfAbsent(key, s -> new ArrayList()).add(value);
    }
    
    void merge(final Store store) {
        if (store != null) {
            for (final String indexName : store.keySet()) {
                final Map<String, Collection<String>> index = store.get(indexName);
                if (index != null) {
                    for (final String key : index.keySet()) {
                        for (final String string : index.get(key)) {
                            this.put(indexName, key, string);
                        }
                    }
                }
            }
        }
    }
}
