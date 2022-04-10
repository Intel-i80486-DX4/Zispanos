//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "/Users/imac/Downloads/Minecraft-Deobfuscator3000-master/1.12 stable mappings"!

//Decompiled by Procyon!

package me.zeroeightsix.kami.module;

import me.zeroeightsix.kami.module.modules.*;
import java.lang.reflect.*;
import me.zeroeightsix.kami.*;
import java.util.function.*;
import net.minecraftforge.client.event.*;
import net.minecraft.client.*;
import net.minecraft.entity.*;
import java.util.*;
import me.zeroeightsix.kami.util.*;
import me.zeroeightsix.kami.event.events.*;
import net.minecraft.client.renderer.*;
import net.minecraft.util.math.*;

public class ModuleManager
{
    public static ArrayList<Module> modules;
    static HashMap<String, Integer> lookup;
    
    public static void updateLookup() {
        ModuleManager.lookup.clear();
        for (int i = 0; i < ModuleManager.modules.size(); ++i) {
            ModuleManager.lookup.put(ModuleManager.modules.get(i).getOriginalName().toLowerCase(), i);
        }
    }
    
    public static void initialize() {
        final Set<Class> classList = (Set<Class>)ClassFinder.findClasses(ClickGUI.class.getPackage().getName(), (Class)Module.class);
        Module module;
        classList.forEach(aClass -> {
            try {
                module = aClass.getConstructor((Class<?>[])new Class[0]).newInstance(new Object[0]);
                ModuleManager.modules.add(module);
            }
            catch (InvocationTargetException e) {
                e.getCause().printStackTrace();
                System.err.println("Couldn't initiate module " + aClass.getSimpleName() + "! Err: " + e.getClass().getSimpleName() + ", message: " + e.getMessage());
            }
            catch (Exception e2) {
                e2.printStackTrace();
                System.err.println("Couldn't initiate module " + aClass.getSimpleName() + "! Err: " + e2.getClass().getSimpleName() + ", message: " + e2.getMessage());
            }
            return;
        });
        KamiMod.log.info("Modules initialised");
        getModules().sort(Comparator.comparing((Function<? super Module, ? extends Comparable>)Module::getOriginalName));
        updateLookup();
    }
    
    public static void onUpdate() {
        ModuleManager.modules.stream().filter(module -> module.alwaysListening || module.isEnabled()).forEach(Module::onUpdate);
    }
    
    public static void onRender() {
        ModuleManager.modules.stream().filter(module -> module.alwaysListening || module.isEnabled()).forEach(Module::onRender);
    }
    
    public static void onWorldRender(final RenderWorldLastEvent event) {
        Minecraft.getMinecraft().profiler.startSection("kami");
        Minecraft.getMinecraft().profiler.startSection("setup");
        GlStateManager.disableTexture2D();
        GlStateManager.enableBlend();
        GlStateManager.disableAlpha();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GlStateManager.shadeModel(7425);
        GlStateManager.disableDepth();
        GlStateManager.glLineWidth(1.0f);
        final Vec3d renderPos = EntityUtil.getInterpolatedPos((Entity)Objects.requireNonNull(Wrapper.getMinecraft().getRenderViewEntity()), event.getPartialTicks());
        final RenderEvent e = new RenderEvent((Tessellator)KamiTessellator.INSTANCE, renderPos);
        e.resetTranslation();
        Minecraft.getMinecraft().profiler.endSection();
        final RenderEvent event2;
        ModuleManager.modules.stream().filter(module -> module.alwaysListening || module.isEnabled()).forEach(module -> {
            Minecraft.getMinecraft().profiler.startSection(module.getOriginalName());
            module.onWorldRender(event2);
            Minecraft.getMinecraft().profiler.endSection();
            return;
        });
        Minecraft.getMinecraft().profiler.startSection("release");
        GlStateManager.glLineWidth(1.0f);
        GlStateManager.shadeModel(7424);
        GlStateManager.disableBlend();
        GlStateManager.enableAlpha();
        GlStateManager.enableTexture2D();
        GlStateManager.enableDepth();
        GlStateManager.enableCull();
        KamiTessellator.releaseGL();
        Minecraft.getMinecraft().profiler.endSection();
    }
    
    public static void onBind(final int eventKey) {
        if (eventKey == 0) {
            return;
        }
        ModuleManager.modules.forEach(module -> {
            if (module.getBind().isDown(eventKey)) {
                module.toggle();
            }
        });
    }
    
    public static ArrayList<Module> getModules() {
        return ModuleManager.modules;
    }
    
    public static Module getModuleByName(final String name) {
        final Integer index = ModuleManager.lookup.get(name.toLowerCase());
        if (index == null) {
            throw new IllegalArgumentException("getModuleByName() failed. Are you calling this too early? Is the module spelled correctly? Please check!!!!");
        }
        return ModuleManager.modules.get(index);
    }
    
    public static boolean isModuleEnabled(final String moduleName) {
        final Module m = getModuleByName(moduleName);
        return m != null && m.isEnabled();
    }
    
    static {
        ModuleManager.modules = new ArrayList<Module>();
        ModuleManager.lookup = new HashMap<String, Integer>();
    }
}
