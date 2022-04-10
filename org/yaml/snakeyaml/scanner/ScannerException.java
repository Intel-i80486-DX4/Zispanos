//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "/Users/imac/Downloads/Minecraft-Deobfuscator3000-master/1.12 stable mappings"!

//Decompiled by Procyon!

package org.yaml.snakeyaml.scanner;

import org.yaml.snakeyaml.error.*;

public class ScannerException extends MarkedYAMLException
{
    private static final long serialVersionUID = 4782293188600445954L;
    
    public ScannerException(final String context, final Mark contextMark, final String problem, final Mark problemMark, final String note) {
        super(context, contextMark, problem, problemMark, note);
    }
    
    public ScannerException(final String context, final Mark contextMark, final String problem, final Mark problemMark) {
        this(context, contextMark, problem, problemMark, (String)null);
    }
}
