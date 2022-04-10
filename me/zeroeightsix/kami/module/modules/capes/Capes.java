//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "/Users/imac/Downloads/Minecraft-Deobfuscator3000-master/1.12 stable mappings"!

//Decompiled by Procyon!

package me.zeroeightsix.kami.module.modules.capes;

import me.zeroeightsix.kami.module.*;
import me.zeroeightsix.kami.setting.*;
import java.util.*;
import javax.net.ssl.*;
import java.net.*;
import com.google.gson.*;
import me.zeroeightsix.kami.*;
import net.minecraft.client.entity.*;
import net.minecraft.util.*;
import java.awt.image.*;
import java.awt.*;
import me.zeroeightsix.kami.command.*;
import me.zeroeightsix.kami.util.*;
import net.minecraft.client.renderer.*;
import java.io.*;
import net.minecraft.client.renderer.texture.*;

@Info(name = "Capes", category = Category.GUI, description = "Controls the display of KAMI Blue capes", showOnArray = ShowOnArray.OFF)
public class Capes extends Module
{
    public Setting<Boolean> startupGlobal;
    public Setting<Boolean> overrideOtherCapes;
    public static Capes INSTANCE;
    private Map<String, CachedCape> allCapes;
    private boolean hasBegunDownload;
    
    public Capes() {
        this.startupGlobal = this.register(Settings.b("Enable Automatically", true));
        this.overrideOtherCapes = Settings.b("Override Mojang / Opti capes", false);
        this.allCapes = Collections.unmodifiableMap((Map<? extends String, ? extends CachedCape>)new HashMap<String, CachedCape>());
        this.hasBegunDownload = false;
        Capes.INSTANCE = this;
    }
    
    public void onEnable() {
        if (!this.hasBegunDownload) {
            this.hasBegunDownload = true;
            new Thread() {
                @Override
                public void run() {
                    try {
                        final HttpsURLConnection connection = (HttpsURLConnection)new URL("https://raw.githubusercontent.com/S-B99/kamiblue/assets/assets/capes.json").openConnection();
                        connection.connect();
                        final CapeUser[] capeUser = (CapeUser[])new Gson().fromJson((Reader)new InputStreamReader(connection.getInputStream()), (Class)CapeUser[].class);
                        connection.disconnect();
                        final HashMap<String, CachedCape> capesByURL = new HashMap<String, CachedCape>();
                        final HashMap<String, CachedCape> capesByUUID = new HashMap<String, CachedCape>();
                        for (final CapeUser cape : capeUser) {
                            CachedCape o = capesByURL.get(cape.url);
                            if (o == null) {
                                o = new CachedCape(cape);
                                capesByURL.put(cape.url, o);
                            }
                            capesByUUID.put(cape.uuid, o);
                        }
                        Capes.this.allCapes = (Map<String, CachedCape>)Collections.unmodifiableMap((Map<?, ?>)capesByUUID);
                    }
                    catch (Exception e) {
                        KamiMod.log.error("Failed to load capes");
                    }
                }
            }.start();
        }
    }
    
    public static ResourceLocation getCapeResource(final AbstractClientPlayer player) {
        final CachedCape result = Capes.INSTANCE.allCapes.get(player.getUniqueID().toString());
        if (result == null) {
            return null;
        }
        result.request();
        return result.location;
    }
    
    private static BufferedImage parseCape(final BufferedImage img) {
        int imageWidth = 64;
        int imageHeight = 32;
        for (int srcWidth = img.getWidth(), srcHeight = img.getHeight(); imageWidth < srcWidth || imageHeight < srcHeight; imageWidth *= 2, imageHeight *= 2) {}
        final BufferedImage imgNew = new BufferedImage(imageWidth, imageHeight, 2);
        final Graphics g = imgNew.getGraphics();
        g.drawImage(img, 0, 0, null);
        g.dispose();
        return imgNew;
    }
    
    private static String formatUUID(final String uuid) {
        return uuid.replaceAll("-", "");
    }
    
    public void onDisable() {
        Command.sendAutoDisableMessage(this.getName(), this.startupGlobal.getValue());
    }
    
    public class CapeUser
    {
        public String uuid;
        public String url;
    }
    
    private static class CachedCape
    {
        public final ResourceLocation location;
        public final String url;
        private boolean hasRequested;
        
        public CachedCape(final CapeUser cape) {
            this.hasRequested = false;
            this.location = new ResourceLocation("capes/kami/" + formatUUID(cape.uuid));
            this.url = cape.url;
        }
        
        public void request() {
            if (this.hasRequested) {
                return;
            }
            this.hasRequested = true;
            final IImageBuffer iib = (IImageBuffer)new IImageBuffer() {
                public BufferedImage parseUserSkin(final BufferedImage image) {
                    return parseCape(image);
                }
                
                public void skinAvailable() {
                }
            };
            final TextureManager textureManager = Wrapper.getMinecraft().getTextureManager();
            textureManager.getTexture(this.location);
            final ThreadDownloadImageData textureCape = new ThreadDownloadImageData((File)null, this.url, (ResourceLocation)null, iib);
            textureManager.loadTexture(this.location, (ITextureObject)textureCape);
        }
    }
}
