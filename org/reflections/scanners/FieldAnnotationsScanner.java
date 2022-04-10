//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "/Users/imac/Downloads/Minecraft-Deobfuscator3000-master/1.12 stable mappings"!

//Decompiled by Procyon!

package org.reflections.scanners;

import org.reflections.*;
import java.util.*;

public class FieldAnnotationsScanner extends AbstractScanner
{
    @Override
    public void scan(final Object cls, final Store store) {
        final String className = this.getMetadataAdapter().getClassName(cls);
        final List<Object> fields = (List<Object>)this.getMetadataAdapter().getFields(cls);
        for (final Object field : fields) {
            final List<String> fieldAnnotations = (List<String>)this.getMetadataAdapter().getFieldAnnotationNames(field);
            for (final String fieldAnnotation : fieldAnnotations) {
                if (this.acceptResult(fieldAnnotation)) {
                    final String fieldName = this.getMetadataAdapter().getFieldName(field);
                    this.put(store, fieldAnnotation, String.format("%s.%s", className, fieldName));
                }
            }
        }
    }
}
