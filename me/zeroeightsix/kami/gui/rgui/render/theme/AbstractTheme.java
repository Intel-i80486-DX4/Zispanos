//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "/Users/imac/Downloads/Minecraft-Deobfuscator3000-master/1.12 stable mappings"!

//Decompiled by Procyon!

package me.zeroeightsix.kami.gui.rgui.render.theme;

import me.zeroeightsix.kami.gui.rgui.component.*;
import me.zeroeightsix.kami.gui.rgui.render.*;
import me.zeroeightsix.kami.gui.rgui.layout.*;
import java.util.*;

public abstract class AbstractTheme implements Theme
{
    protected final Map<Class<? extends Component>, ComponentUI> uis;
    protected final Map<Class<? extends Layout>, Class<? extends Layout>> layoutMap;
    
    public AbstractTheme() {
        this.uis = new HashMap<Class<? extends Component>, ComponentUI>();
        this.layoutMap = new HashMap<Class<? extends Layout>, Class<? extends Layout>>();
    }
    
    protected void installUI(final ComponentUI<?> ui) {
        this.uis.put(ui.getHandledClass(), ui);
    }
    
    @Override
    public ComponentUI getUIForComponent(final Component component) {
        final ComponentUI a = this.getComponentUIForClass(component.getClass());
        if (a == null) {
            throw new RuntimeException("No installed component UI for " + component.getClass().getName());
        }
        return a;
    }
    
    public ComponentUI getComponentUIForClass(final Class<? extends Component> componentClass) {
        if (this.uis.containsKey(componentClass)) {
            return this.uis.get(componentClass);
        }
        if (componentClass == null) {
            return null;
        }
        for (final Class<?> componentInterface : componentClass.getInterfaces()) {
            final ComponentUI ui = this.uis.get(componentInterface);
            if (ui != null) {
                return ui;
            }
        }
        return this.getComponentUIForClass((Class<? extends Component>)componentClass.getSuperclass());
    }
}
