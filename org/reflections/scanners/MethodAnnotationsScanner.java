//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "/Users/imac/Downloads/Minecraft-Deobfuscator3000-master/1.12 stable mappings"!

//Decompiled by Procyon!

package org.reflections.scanners;

import org.reflections.*;
import java.util.*;

public class MethodAnnotationsScanner extends AbstractScanner
{
    @Override
    public void scan(final Object cls, final Store store) {
        for (final Object method : this.getMetadataAdapter().getMethods(cls)) {
            for (final String methodAnnotation : this.getMetadataAdapter().getMethodAnnotationNames(method)) {
                if (this.acceptResult(methodAnnotation)) {
                    this.put(store, methodAnnotation, this.getMetadataAdapter().getMethodFullKey(cls, method));
                }
            }
        }
    }
}
