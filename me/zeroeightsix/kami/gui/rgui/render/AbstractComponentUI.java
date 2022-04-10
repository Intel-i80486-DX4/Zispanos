//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "/Users/imac/Downloads/Minecraft-Deobfuscator3000-master/1.12 stable mappings"!

//Decompiled by Procyon!

package me.zeroeightsix.kami.gui.rgui.render;

import me.zeroeightsix.kami.gui.rgui.component.*;
import java.lang.reflect.*;
import me.zeroeightsix.kami.gui.rgui.render.font.*;
import me.zeroeightsix.kami.gui.rgui.component.container.*;

public abstract class AbstractComponentUI<T extends Component> implements ComponentUI<T>
{
    private Class<T> persistentClass;
    
    public AbstractComponentUI() {
        this.persistentClass = (Class<T>)((ParameterizedType)this.getClass().getGenericSuperclass()).getActualTypeArguments()[0];
    }
    
    public void renderComponent(final T component, final FontRenderer fontRenderer) {
    }
    
    public void handleMouseDown(final T component, final int x, final int y, final int button) {
    }
    
    public void handleMouseRelease(final T component, final int x, final int y, final int button) {
    }
    
    public void handleMouseDrag(final T component, final int x, final int y, final int button) {
    }
    
    public void handleScroll(final T component, final int x, final int y, final int amount, final boolean up) {
    }
    
    public void handleAddComponent(final T component, final Container container) {
    }
    
    public void handleKeyDown(final T component, final int key) {
    }
    
    public void handleKeyUp(final T component, final int key) {
    }
    
    public void handleSizeComponent(final T component) {
    }
    
    public Class<? extends Component> getHandledClass() {
        return this.persistentClass;
    }
}
