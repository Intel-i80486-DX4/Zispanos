//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "/Users/imac/Downloads/Minecraft-Deobfuscator3000-master/1.12 stable mappings"!

//Decompiled by Procyon!

package org.reflections;

import org.slf4j.*;
import java.net.*;
import java.util.concurrent.*;
import org.reflections.vfs.*;
import java.util.function.*;
import org.reflections.serializers.*;
import org.reflections.util.*;
import java.io.*;
import java.lang.annotation.*;
import java.util.stream.*;
import java.util.regex.*;
import java.lang.reflect.*;
import org.reflections.scanners.*;
import java.util.*;

public class Reflections
{
    public static Logger log;
    protected final transient Configuration configuration;
    protected Store store;
    
    public Reflections(final Configuration configuration) {
        this.configuration = configuration;
        this.store = new Store();
        if (configuration.getScanners() != null && !configuration.getScanners().isEmpty()) {
            for (final Scanner scanner : configuration.getScanners()) {
                scanner.setConfiguration(configuration);
            }
            this.scan();
            if (configuration.shouldExpandSuperTypes()) {
                this.expandSuperTypes();
            }
        }
    }
    
    public Reflections(final String prefix, final Scanner... scanners) {
        this(new Object[] { prefix, scanners });
    }
    
    public Reflections(final Object... params) {
        this((Configuration)ConfigurationBuilder.build(params));
    }
    
    protected Reflections() {
        this.configuration = (Configuration)new ConfigurationBuilder();
        this.store = new Store();
    }
    
    protected void scan() {
        if (this.configuration.getUrls() == null || this.configuration.getUrls().isEmpty()) {
            if (Reflections.log != null) {
                Reflections.log.warn("given scan urls are empty. set urls in the configuration");
            }
            return;
        }
        if (Reflections.log != null && Reflections.log.isDebugEnabled()) {
            Reflections.log.debug("going to scan these urls: {}", (Object)this.configuration.getUrls());
        }
        final long time = System.currentTimeMillis();
        int scannedUrls = 0;
        final ExecutorService executorService = this.configuration.getExecutorService();
        final List<Future<?>> futures = new ArrayList<Future<?>>();
        for (final URL url : this.configuration.getUrls()) {
            try {
                if (executorService != null) {
                    final URL url2;
                    futures.add(executorService.submit(() -> {
                        if (Reflections.log != null) {
                            Reflections.log.debug("[{}] scanning {}", (Object)Thread.currentThread().toString(), (Object)url2);
                        }
                        this.scan(url2);
                        return;
                    }));
                }
                else {
                    this.scan(url);
                }
                ++scannedUrls;
            }
            catch (ReflectionsException e) {
                if (Reflections.log == null) {
                    continue;
                }
                Reflections.log.warn("could not create Vfs.Dir from url. ignoring the exception and continuing", (Throwable)e);
            }
        }
        if (executorService != null) {
            for (final Future future : futures) {
                try {
                    future.get();
                }
                catch (Exception e2) {
                    throw new RuntimeException(e2);
                }
            }
        }
        if (executorService != null) {
            executorService.shutdown();
        }
        if (Reflections.log != null) {
            Reflections.log.info(String.format("Reflections took %d ms to scan %d urls, producing %s %s", System.currentTimeMillis() - time, scannedUrls, producingDescription(this.store), (executorService instanceof ThreadPoolExecutor) ? String.format("[using %d cores]", ((ThreadPoolExecutor)executorService).getMaximumPoolSize()) : ""));
        }
    }
    
    private static String producingDescription(final Store store) {
        int keys = 0;
        int values = 0;
        for (final String index : store.keySet()) {
            keys += store.keys(index).size();
            values += store.values(index).size();
        }
        return String.format("%d keys and %d values", keys, values);
    }
    
    protected void scan(final URL url) {
        final Vfs.Dir dir = Vfs.fromURL(url);
        try {
            for (final Vfs.File file : dir.getFiles()) {
                final Predicate<String> inputsFilter = this.configuration.getInputsFilter();
                final String path = file.getRelativePath();
                final String fqn = path.replace('/', '.');
                if (inputsFilter == null || inputsFilter.test(path) || inputsFilter.test(fqn)) {
                    Object classObject = null;
                    for (final Scanner scanner : this.configuration.getScanners()) {
                        try {
                            if (!scanner.acceptsInput(path) && !scanner.acceptsInput(fqn)) {
                                continue;
                            }
                            classObject = scanner.scan(file, classObject, this.store);
                        }
                        catch (Exception e) {
                            if (Reflections.log == null) {
                                continue;
                            }
                            Reflections.log.debug("could not scan file {} in url {} with scanner {}", new Object[] { file.getRelativePath(), url.toExternalForm(), scanner.getClass().getSimpleName(), e });
                        }
                    }
                }
            }
        }
        finally {
            dir.close();
        }
    }
    
    public static Reflections collect() {
        return collect("META-INF/reflections/", (Predicate<String>)new FilterBuilder().include(".*-reflections.xml"), new Serializer[0]);
    }
    
    public static Reflections collect(final String packagePrefix, final Predicate<String> resourceNameFilter, final Serializer... optionalSerializer) {
        final Serializer serializer = (Serializer)((optionalSerializer != null && optionalSerializer.length == 1) ? optionalSerializer[0] : new XmlSerializer());
        final Collection<URL> urls = (Collection<URL>)ClasspathHelper.forPackage(packagePrefix, new ClassLoader[0]);
        if (urls.isEmpty()) {
            return null;
        }
        final long start = System.currentTimeMillis();
        final Reflections reflections = new Reflections();
        final Iterable<Vfs.File> files = (Iterable<Vfs.File>)Vfs.findFiles((Collection)urls, packagePrefix, (Predicate)resourceNameFilter);
        for (final Vfs.File file : files) {
            InputStream inputStream = null;
            try {
                inputStream = file.openInputStream();
                reflections.merge(serializer.read(inputStream));
            }
            catch (IOException e) {
                throw new ReflectionsException("could not merge " + file, (Throwable)e);
            }
            finally {
                Utils.close(inputStream);
            }
        }
        if (Reflections.log != null) {
            Reflections.log.info(String.format("Reflections took %d ms to collect %d url, producing %s", System.currentTimeMillis() - start, urls.size(), producingDescription(reflections.store)));
        }
        return reflections;
    }
    
    public Reflections collect(final InputStream inputStream) {
        try {
            this.merge(this.configuration.getSerializer().read(inputStream));
            if (Reflections.log != null) {
                Reflections.log.info("Reflections collected metadata from input stream using serializer " + this.configuration.getSerializer().getClass().getName());
            }
        }
        catch (Exception ex) {
            throw new ReflectionsException("could not merge input stream", (Throwable)ex);
        }
        return this;
    }
    
    public Reflections collect(final File file) {
        FileInputStream inputStream = null;
        try {
            inputStream = new FileInputStream(file);
            return this.collect(inputStream);
        }
        catch (FileNotFoundException e) {
            throw new ReflectionsException("could not obtain input stream from file " + file, (Throwable)e);
        }
        finally {
            Utils.close((InputStream)inputStream);
        }
    }
    
    public Reflections merge(final Reflections reflections) {
        this.store.merge(reflections.store);
        return this;
    }
    
    public void expandSuperTypes() {
        final String index = Utils.index((Class)SubTypesScanner.class);
        final Set<String> keys = (Set<String>)this.store.keys(index);
        keys.removeAll(this.store.values(index));
        for (final String key : keys) {
            final Class<?> type = (Class<?>)ReflectionUtils.forName(key, this.loaders());
            if (type != null) {
                this.expandSupertypes(this.store, key, type);
            }
        }
    }
    
    private void expandSupertypes(final Store store, final String key, final Class<?> type) {
        for (final Class<?> supertype : ReflectionUtils.getSuperTypes((Class)type)) {
            if (store.put((Class)SubTypesScanner.class, supertype.getName(), key)) {
                if (Reflections.log != null) {
                    Reflections.log.debug("expanded subtype {} -> {}", (Object)supertype.getName(), (Object)key);
                }
                this.expandSupertypes(store, supertype.getName(), supertype);
            }
        }
    }
    
    public <T> Set<Class<? extends T>> getSubTypesOf(final Class<T> type) {
        return (Set<Class<? extends T>>)ReflectionUtils.forNames((Collection)this.store.getAll((Class)SubTypesScanner.class, type.getName()), this.loaders());
    }
    
    public Set<Class<?>> getTypesAnnotatedWith(final Class<? extends Annotation> annotation) {
        return this.getTypesAnnotatedWith(annotation, false);
    }
    
    public Set<Class<?>> getTypesAnnotatedWith(final Class<? extends Annotation> annotation, final boolean honorInherited) {
        final Set<String> annotated = (Set<String>)this.store.get((Class)TypeAnnotationsScanner.class, annotation.getName());
        annotated.addAll(this.getAllAnnotated(annotated, annotation, honorInherited));
        return (Set<Class<?>>)ReflectionUtils.forNames((Collection)annotated, this.loaders());
    }
    
    public Set<Class<?>> getTypesAnnotatedWith(final Annotation annotation) {
        return this.getTypesAnnotatedWith(annotation, false);
    }
    
    public Set<Class<?>> getTypesAnnotatedWith(final Annotation annotation, final boolean honorInherited) {
        final Set<String> annotated = (Set<String>)this.store.get((Class)TypeAnnotationsScanner.class, annotation.annotationType().getName());
        final Set<Class<?>> allAnnotated = (Set<Class<?>>)Utils.filter((Collection)ReflectionUtils.forNames((Collection)annotated, this.loaders()), ReflectionUtils.withAnnotation(annotation));
        final Set<Class<?>> classes = (Set<Class<?>>)ReflectionUtils.forNames((Collection)Utils.filter((Collection)this.getAllAnnotated(Utils.names((Collection)allAnnotated), annotation.annotationType(), honorInherited), s -> !annotated.contains(s)), this.loaders());
        allAnnotated.addAll(classes);
        return allAnnotated;
    }
    
    protected Collection<String> getAllAnnotated(final Collection<String> annotated, final Class<? extends Annotation> annotation, final boolean honorInherited) {
        if (!honorInherited) {
            final Collection<String> subTypes = (Collection<String>)this.store.getAllIncluding((Class)TypeAnnotationsScanner.class, (Collection)annotated);
            return (Collection<String>)this.store.getAllIncluding((Class)SubTypesScanner.class, (Collection)subTypes);
        }
        if (annotation.isAnnotationPresent(Inherited.class)) {
            final Class<?> type;
            final Set<String> subTypes2 = (Set<String>)this.store.get((Class)SubTypesScanner.class, (Collection)Utils.filter((Collection)annotated, input -> {
                type = (Class<?>)ReflectionUtils.forName(input, this.loaders());
                return type != null && !type.isInterface();
            }));
            return (Collection<String>)this.store.getAllIncluding((Class)SubTypesScanner.class, (Collection)subTypes2);
        }
        return annotated;
    }
    
    public Set<Method> getMethodsAnnotatedWith(final Class<? extends Annotation> annotation) {
        return (Set<Method>)Utils.getMethodsFromDescriptors((Iterable)this.store.get((Class)MethodAnnotationsScanner.class, annotation.getName()), this.loaders());
    }
    
    public Set<Method> getMethodsAnnotatedWith(final Annotation annotation) {
        return (Set<Method>)Utils.filter((Collection)this.getMethodsAnnotatedWith(annotation.annotationType()), ReflectionUtils.withAnnotation(annotation));
    }
    
    public Set<Method> getMethodsMatchParams(final Class<?>... types) {
        return (Set<Method>)Utils.getMethodsFromDescriptors((Iterable)this.store.get((Class)MethodParameterScanner.class, Utils.names((Class[])types).toString()), this.loaders());
    }
    
    public Set<Method> getMethodsReturn(final Class returnType) {
        return (Set<Method>)Utils.getMethodsFromDescriptors((Iterable)this.store.get((Class)MethodParameterScanner.class, (Collection)Utils.names(new Class[] { returnType })), this.loaders());
    }
    
    public Set<Method> getMethodsWithAnyParamAnnotated(final Class<? extends Annotation> annotation) {
        return (Set<Method>)Utils.getMethodsFromDescriptors((Iterable)this.store.get((Class)MethodParameterScanner.class, annotation.getName()), this.loaders());
    }
    
    public Set<Method> getMethodsWithAnyParamAnnotated(final Annotation annotation) {
        return (Set<Method>)Utils.filter((Collection)this.getMethodsWithAnyParamAnnotated(annotation.annotationType()), ReflectionUtils.withAnyParameterAnnotation(annotation));
    }
    
    public Set<Constructor> getConstructorsAnnotatedWith(final Class<? extends Annotation> annotation) {
        return (Set<Constructor>)Utils.getConstructorsFromDescriptors((Iterable)this.store.get((Class)MethodAnnotationsScanner.class, annotation.getName()), this.loaders());
    }
    
    public Set<Constructor> getConstructorsAnnotatedWith(final Annotation annotation) {
        return (Set<Constructor>)Utils.filter((Collection)this.getConstructorsAnnotatedWith(annotation.annotationType()), ReflectionUtils.withAnnotation(annotation));
    }
    
    public Set<Constructor> getConstructorsMatchParams(final Class<?>... types) {
        return (Set<Constructor>)Utils.getConstructorsFromDescriptors((Iterable)this.store.get((Class)MethodParameterScanner.class, Utils.names((Class[])types).toString()), this.loaders());
    }
    
    public Set<Constructor> getConstructorsWithAnyParamAnnotated(final Class<? extends Annotation> annotation) {
        return (Set<Constructor>)Utils.getConstructorsFromDescriptors((Iterable)this.store.get((Class)MethodParameterScanner.class, annotation.getName()), this.loaders());
    }
    
    public Set<Constructor> getConstructorsWithAnyParamAnnotated(final Annotation annotation) {
        return (Set<Constructor>)Utils.filter((Collection)this.getConstructorsWithAnyParamAnnotated(annotation.annotationType()), ReflectionUtils.withAnyParameterAnnotation(annotation));
    }
    
    public Set<Field> getFieldsAnnotatedWith(final Class<? extends Annotation> annotation) {
        return (Set<Field>)this.store.get((Class)FieldAnnotationsScanner.class, annotation.getName()).stream().map(annotated -> Utils.getFieldFromString(annotated, this.loaders())).collect(Collectors.toSet());
    }
    
    public Set<Field> getFieldsAnnotatedWith(final Annotation annotation) {
        return (Set<Field>)Utils.filter((Collection)this.getFieldsAnnotatedWith(annotation.annotationType()), ReflectionUtils.withAnnotation(annotation));
    }
    
    public Set<String> getResources(final Predicate<String> namePredicate) {
        final Set<String> resources = (Set<String>)Utils.filter((Collection)this.store.keys(Utils.index((Class)ResourcesScanner.class)), (Predicate)namePredicate);
        return (Set<String>)this.store.get((Class)ResourcesScanner.class, (Collection)resources);
    }
    
    public Set<String> getResources(final Pattern pattern) {
        return this.getResources(input -> pattern.matcher(input).matches());
    }
    
    public List<String> getMethodParamNames(final Method method) {
        final Set<String> names = (Set<String>)this.store.get((Class)MethodParameterNamesScanner.class, Utils.name(method));
        return (names.size() == 1) ? Arrays.asList(names.iterator().next().split(", ")) : Collections.emptyList();
    }
    
    public List<String> getConstructorParamNames(final Constructor constructor) {
        final Set<String> names = (Set<String>)this.store.get((Class)MethodParameterNamesScanner.class, Utils.name(constructor));
        return (names.size() == 1) ? Arrays.asList(names.iterator().next().split(", ")) : Collections.emptyList();
    }
    
    public Set<Member> getFieldUsage(final Field field) {
        return (Set<Member>)Utils.getMembersFromDescriptors((Iterable)this.store.get((Class)MemberUsageScanner.class, Utils.name(field)), new ClassLoader[0]);
    }
    
    public Set<Member> getMethodUsage(final Method method) {
        return (Set<Member>)Utils.getMembersFromDescriptors((Iterable)this.store.get((Class)MemberUsageScanner.class, Utils.name(method)), new ClassLoader[0]);
    }
    
    public Set<Member> getConstructorUsage(final Constructor constructor) {
        return (Set<Member>)Utils.getMembersFromDescriptors((Iterable)this.store.get((Class)MemberUsageScanner.class, Utils.name(constructor)), new ClassLoader[0]);
    }
    
    public Set<String> getAllTypes() {
        final Set<String> allTypes = new HashSet<String>(this.store.getAll((Class)SubTypesScanner.class, Object.class.getName()));
        if (allTypes.isEmpty()) {
            throw new ReflectionsException("Couldn't find subtypes of Object. Make sure SubTypesScanner initialized to include Object class - new SubTypesScanner(false)");
        }
        return allTypes;
    }
    
    public Store getStore() {
        return this.store;
    }
    
    public Configuration getConfiguration() {
        return this.configuration;
    }
    
    public File save(final String filename) {
        return this.save(filename, this.configuration.getSerializer());
    }
    
    public File save(final String filename, final Serializer serializer) {
        final File file = serializer.save(this, filename);
        if (Reflections.log != null) {
            Reflections.log.info("Reflections successfully saved in " + file.getAbsolutePath() + " using " + serializer.getClass().getSimpleName());
        }
        return file;
    }
    
    private ClassLoader[] loaders() {
        return this.configuration.getClassLoaders();
    }
    
    static {
        Reflections.log = Utils.findLogger((Class)Reflections.class);
    }
}
