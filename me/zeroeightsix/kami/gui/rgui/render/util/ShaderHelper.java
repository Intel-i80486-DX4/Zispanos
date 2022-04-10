//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "/Users/imac/Downloads/Minecraft-Deobfuscator3000-master/1.12 stable mappings"!

//Decompiled by Procyon!

package me.zeroeightsix.kami.gui.rgui.render.util;

import org.lwjgl.opengl.*;

public final class ShaderHelper
{
    private ShaderHelper() {
    }
    
    public static void createProgram(final int programID) {
        ARBShaderObjects.glLinkProgramARB(programID);
        checkObjecti(programID, 35714);
        ARBShaderObjects.glValidateProgramARB(programID);
        checkObjecti(programID, 35715);
    }
    
    public static int loadShader(final String path, final int type) {
        final int shaderID = ARBShaderObjects.glCreateShaderObjectARB(type);
        if (shaderID == 0) {
            return 0;
        }
        final String src = new StreamReader(ShaderHelper.class.getResourceAsStream(path)).read();
        ARBShaderObjects.glShaderSourceARB(shaderID, (CharSequence)src);
        ARBShaderObjects.glCompileShaderARB(shaderID);
        checkObjecti(shaderID, 35713);
        return shaderID;
    }
    
    private static String getLogInfo(final int objID) {
        return ARBShaderObjects.glGetInfoLogARB(objID, ARBShaderObjects.glGetObjectParameteriARB(objID, 35716));
    }
    
    private static void checkObjecti(final int objID, final int name) {
        if (ARBShaderObjects.glGetObjectParameteriARB(objID, name) == 0) {
            try {
                throw new Exception(getLogInfo(objID));
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
