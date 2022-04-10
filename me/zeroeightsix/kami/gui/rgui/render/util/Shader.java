//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "/Users/imac/Downloads/Minecraft-Deobfuscator3000-master/1.12 stable mappings"!

//Decompiled by Procyon!

package me.zeroeightsix.kami.gui.rgui.render.util;

import java.util.*;
import org.lwjgl.opengl.*;

public abstract class Shader
{
    private final Map<String, Uniform> uniforms;
    private final int programID;
    private final int fragmentID;
    private final int vertexID;
    
    public Shader(final String vertex, final String fragment) {
        this.uniforms = new HashMap<String, Uniform>();
        this.programID = ARBShaderObjects.glCreateProgramObjectARB();
        this.vertexID = ShaderHelper.loadShader(vertex, 35633);
        this.fragmentID = ShaderHelper.loadShader(fragment, 35632);
        ARBShaderObjects.glAttachObjectARB(this.programID, this.vertexID);
        ARBShaderObjects.glAttachObjectARB(this.programID, this.fragmentID);
        ShaderHelper.createProgram(this.programID);
    }
    
    public final void attach() {
        ARBShaderObjects.glUseProgramObjectARB(this.programID);
        this.update();
    }
    
    public final void detach() {
        ARBShaderObjects.glUseProgramObjectARB(0);
    }
    
    public abstract void update();
    
    public final void delete() {
        ARBShaderObjects.glUseProgramObjectARB(0);
        ARBShaderObjects.glDetachObjectARB(this.programID, this.vertexID);
        ARBShaderObjects.glDetachObjectARB(this.programID, this.fragmentID);
        ARBShaderObjects.glDeleteObjectARB(this.vertexID);
        ARBShaderObjects.glDeleteObjectARB(this.fragmentID);
        ARBShaderObjects.glDeleteObjectARB(this.programID);
    }
    
    protected final Uniform getUniform(final String name) {
        return this.uniforms.computeIfAbsent(name, n -> Uniform.get(this.programID, n));
    }
}
