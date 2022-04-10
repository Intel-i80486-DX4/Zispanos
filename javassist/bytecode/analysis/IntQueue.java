//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "/Users/imac/Downloads/Minecraft-Deobfuscator3000-master/1.12 stable mappings"!

//Decompiled by Procyon!

package javassist.bytecode.analysis;

import java.util.*;

class IntQueue
{
    private Entry head;
    private Entry tail;
    
    void add(final int value) {
        final Entry entry = new Entry(value);
        if (this.tail != null) {
            this.tail.next = entry;
        }
        this.tail = entry;
        if (this.head == null) {
            this.head = entry;
        }
    }
    
    boolean isEmpty() {
        return this.head == null;
    }
    
    int take() {
        if (this.head == null) {
            throw new NoSuchElementException();
        }
        final int value = this.head.value;
        this.head = this.head.next;
        if (this.head == null) {
            this.tail = null;
        }
        return value;
    }
    
    private static class Entry
    {
        private Entry next;
        private int value;
        
        private Entry(final int value) {
            this.value = value;
        }
    }
}
