//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "/Users/imac/Downloads/Minecraft-Deobfuscator3000-master/1.12 stable mappings"!

//Decompiled by Procyon!

package me.zeroeightsix.kami;

import net.minecraftforge.fml.common.*;
import me.zeroeightsix.kami.gui.kami.*;
import com.google.common.base.*;
import org.lwjgl.opengl.*;
import net.minecraftforge.fml.common.event.*;
import net.minecraftforge.common.*;
import me.zeroeightsix.kami.event.*;
import me.zeroeightsix.kami.command.*;
import me.zeroeightsix.kami.setting.*;
import me.zeroeightsix.kami.util.*;
import me.zeroeightsix.kami.module.*;
import java.util.function.*;
import me.zeroeightsix.kami.module.modules.capes.*;
import me.zeroeightsix.kami.module.modules.misc.*;
import me.zeroeightsix.kami.module.modules.hidden.*;
import me.zeroeightsix.kami.module.modules.render.*;
import me.zeroeightsix.kami.module.modules.chat.*;
import me.zeroeightsix.kami.module.modules.gui.*;
import java.nio.file.*;
import me.zeroeightsix.kami.setting.config.*;
import me.zeroeightsix.kami.gui.rgui.component.container.use.*;
import me.zeroeightsix.kami.gui.rgui.util.*;
import me.zeroeightsix.kami.gui.rgui.component.container.*;
import me.zeroeightsix.kami.gui.rgui.component.*;
import com.google.gson.*;
import java.util.*;
import java.nio.file.attribute.*;
import java.io.*;
import org.apache.logging.log4j.*;
import me.zero.alpine.*;

@Mod(modid = "zispanos", name = "Zispanos Backdoor", version = "0.5beta", updateJSON = "https://raw.githubusercontent.com/S-B99/kamiblue/assets/assets/updateChecker.json")
public class KamiMod
{
    public static final String MODNAME = "Zispanos Backdoor";
    public static final String MODID = "zispanos";
    public static final String MODVER = "0.5beta";
    public static final String MODVERSMALL = "0.5beta";
    public static final String APP_ID = "693281610459512844";
    static final String UPDATE_JSON = "https://raw.githubusercontent.com/S-B99/kamiblue/assets/assets/updateChecker.json";
    public static final String DONATORS_JSON = "https://raw.githubusercontent.com/S-B99/kamiblue/assets/assets/donators.json";
    public static final String CAPES_JSON = "https://raw.githubusercontent.com/S-B99/kamiblue/assets/assets/capes.json";
    public static final String GITHUB_LINK = "https://github.com/S-B99/kamiblue/";
    public static final String WEBSITE_LINK = "https://blue.bella.wtf";
    public static final String KAMI_KANJI = "\uff3a\uff49\uff53\uff50\uff41\uff4e\uff4f\uff53";
    public static final String KAMI_BLUE = "\u1d22\u026a\ua731\u1d18\u1d00\u0274\u1d0f\ua731 \u0299\u1d00\u1d04\u1d0b\u1d05\u1d0f\u1d0f\u0280";
    public static final String KAMI_JAPANESE_ONTOP = "\u1d22\u026a\ua731\u1d18\u1d00\u0274\u1d0f\ua731 \u0299\u1d00\u1d04\u1d0b\u1d05\u1d0f\u1d0f\u0280";
    public static final String KAMI_ONTOP = "\u1d22\u026a\ua731\u1d18\u1d00\u0274\u1d0f\ua731 \u0299\u1d00\u1d04\u1d0b\u1d05\u1d0f\u1d0f\u0280";
    public static final String KAMI_WEBSITE = "\u1d22\u026a\ua731\u1d18\u1d00\u0274\u1d0f\ua731 \u0299\u1d00\u1d04\u1d0b\u1d05\u1d0f\u1d0f\u0280";
    public static final char colour = '§';
    public static final char separator = '\u23d0';
    public static final char quoteLeft = '«';
    public static final char quoteRight = '»';
    private static final String KAMI_CONFIG_NAME_DEFAULT = "Zispanos.json";
    public static final Logger log;
    public static final EventBus EVENT_BUS;
    @Mod.Instance
    private static KamiMod INSTANCE;
    public KamiGUI guiManager;
    public CommandManager commandManager;
    private Setting<JsonObject> guiStateSetting;
    
    public KamiMod() {
        this.guiStateSetting = Settings.custom("gui", new JsonObject(), new Converter<JsonObject, JsonObject>() {
            protected JsonObject doForward(final JsonObject jsonObject) {
                return jsonObject;
            }
            
            protected JsonObject doBackward(final JsonObject jsonObject) {
                return jsonObject;
            }
        }).buildAndRegister("");
    }
    
    @Mod.EventHandler
    public void preInit(final FMLPreInitializationEvent event) {
    }
    
    @Mod.EventHandler
    public void postInit(final FMLPostInitializationEvent event) {
        Display.setTitle("Zispanos Backdoor 0.5beta");
    }
    
    @Mod.EventHandler
    public void init(final FMLInitializationEvent event) {
        KamiMod.log.info("\n\nInitializing Zispanos Backdoor 0.5beta");
        ModuleManager.initialize();
        ModuleManager.getModules().stream().filter(module -> module.alwaysListening).forEach(KamiMod.EVENT_BUS::subscribe);
        MinecraftForge.EVENT_BUS.register((Object)new ForgeEventProcessor());
        LagCompensator.INSTANCE = new LagCompensator();
        Wrapper.init();
        (this.guiManager = new KamiGUI()).initializeGUI();
        this.commandManager = new CommandManager();
        Friends.initFriends();
        SettingsRegister.register("commandPrefix", Command.commandPrefix);
        loadConfiguration();
        KamiMod.log.info("Settings loaded");
        new RichPresence();
        KamiMod.log.info("Rich Presence Users init!\n");
        ModuleManager.getModules().stream().filter(Module::isEnabled).forEach(Module::enable);
        try {
            ModuleManager.getModuleByName("InfoOverlay").setEnabled(true);
            ModuleManager.getModuleByName("InventoryViewer").setEnabled(true);
            ModuleManager.getModuleByName("ActiveModules").setEnabled(true);
            if (((Capes)ModuleManager.getModuleByName("Capes")).startupGlobal.getValue()) {
                ModuleManager.getModuleByName("Capes").setEnabled(true);
            }
            if (((DiscordSettings)ModuleManager.getModuleByName("DiscordSettings")).startupGlobal.getValue()) {
                ModuleManager.getModuleByName("DiscordSettings").setEnabled(true);
            }
            if (((FixGui)ModuleManager.getModuleByName("Hidden:FixGui")).shouldAutoEnable.getValue()) {
                ModuleManager.getModuleByName("Hidden:FixGui").setEnabled(true);
            }
            if (((TabFriends)ModuleManager.getModuleByName("TabFriends")).startupGlobal.getValue()) {
                ModuleManager.getModuleByName("TabFriends").setEnabled(true);
            }
            if (((CustomChat)ModuleManager.getModuleByName("CustomChat")).startupGlobal.getValue()) {
                ModuleManager.getModuleByName("CustomChat").setEnabled(true);
            }
            if (((CleanGUI)ModuleManager.getModuleByName("CleanGUI")).startupGlobal.getValue()) {
                ModuleManager.getModuleByName("CleanGUI").setEnabled(true);
            }
            if (((PrefixChat)ModuleManager.getModuleByName("PrefixChat")).startupGlobal.getValue()) {
                ModuleManager.getModuleByName("PrefixChat").setEnabled(true);
            }
        }
        catch (NullPointerException e) {
            KamiMod.log.error("NPE in loading always enabled modules\n");
        }
        KamiMod.log.info("Zispanos Backdoor Mod initialized!\n");
    }
    
    public static String getConfigName() {
        final Path config = Paths.get("ZispanosLastConfig.txt", new String[0]);
        String kamiConfigName = "Zispanos.json";
        try (final BufferedReader reader = Files.newBufferedReader(config)) {
            kamiConfigName = reader.readLine();
            if (!isFilenameValid(kamiConfigName)) {
                kamiConfigName = "Zispanos.json";
            }
        }
        catch (NoSuchFileException e3) {
            try (final BufferedWriter writer = Files.newBufferedWriter(config, new OpenOption[0])) {
                writer.write("Zispanos.json");
            }
            catch (IOException e1) {
                e1.printStackTrace();
            }
        }
        catch (IOException e2) {
            e2.printStackTrace();
        }
        return kamiConfigName;
    }
    
    public static void loadConfiguration() {
        try {
            loadConfigurationUnsafe();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public static void loadConfigurationUnsafe() throws IOException {
        final String kamiConfigName = getConfigName();
        final Path kamiConfig = Paths.get(kamiConfigName, new String[0]);
        if (!Files.exists(kamiConfig, new LinkOption[0])) {
            return;
        }
        Configuration.loadConfiguration(kamiConfig);
        final JsonObject gui = KamiMod.INSTANCE.guiStateSetting.getValue();
        for (final Map.Entry<String, JsonElement> entry : gui.entrySet()) {
            final Optional<Component> optional = KamiMod.INSTANCE.guiManager.getChildren().stream().filter(component -> component instanceof Frame).filter(component -> component.getTitle().equals(entry.getKey())).findFirst();
            if (optional.isPresent()) {
                final JsonObject object = entry.getValue().getAsJsonObject();
                final Frame frame = optional.get();
                frame.setX(object.get("x").getAsInt());
                frame.setY(object.get("y").getAsInt());
                final Docking docking = Docking.values()[object.get("docking").getAsInt()];
                if (docking.isLeft()) {
                    ContainerHelper.setAlignment(frame, AlignedComponent.Alignment.LEFT);
                }
                else if (docking.isRight()) {
                    ContainerHelper.setAlignment(frame, AlignedComponent.Alignment.RIGHT);
                }
                else if (docking.isCenterVertical()) {
                    ContainerHelper.setAlignment(frame, AlignedComponent.Alignment.CENTER);
                }
                frame.setDocking(docking);
                frame.setMinimized(object.get("minimized").getAsBoolean());
                frame.setPinned(object.get("pinned").getAsBoolean());
            }
            else {
                System.err.println("Found GUI config entry for " + entry.getKey() + ", but found no frame with that name");
            }
        }
        getInstance().getGuiManager().getChildren().stream().filter(component -> component instanceof Frame && component.isPinnable() && component.isVisible()).forEach(component -> component.setOpacity(0.0f));
    }
    
    public static void saveConfiguration() {
        try {
            saveConfigurationUnsafe();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public static void saveConfigurationUnsafe() throws IOException {
        final JsonObject object = new JsonObject();
        final JsonObject frameObject;
        final JsonObject jsonObject;
        KamiMod.INSTANCE.guiManager.getChildren().stream().filter(component -> component instanceof Frame).map(component -> component).forEach(frame -> {
            frameObject = new JsonObject();
            frameObject.add("x", (JsonElement)new JsonPrimitive((Number)frame.getX()));
            frameObject.add("y", (JsonElement)new JsonPrimitive((Number)frame.getY()));
            frameObject.add("docking", (JsonElement)new JsonPrimitive((Number)Arrays.asList(Docking.values()).indexOf(frame.getDocking())));
            frameObject.add("minimized", (JsonElement)new JsonPrimitive(Boolean.valueOf(frame.isMinimized())));
            frameObject.add("pinned", (JsonElement)new JsonPrimitive(Boolean.valueOf(frame.isPinned())));
            jsonObject.add(frame.getTitle(), (JsonElement)frameObject);
            return;
        });
        KamiMod.INSTANCE.guiStateSetting.setValue(object);
        final Path outputFile = Paths.get(getConfigName(), new String[0]);
        if (!Files.exists(outputFile, new LinkOption[0])) {
            Files.createFile(outputFile, (FileAttribute<?>[])new FileAttribute[0]);
        }
        Configuration.saveConfiguration(outputFile);
        ModuleManager.getModules().forEach(Module::destroy);
    }
    
    public static boolean isFilenameValid(final String file) {
        final File f = new File(file);
        try {
            f.getCanonicalPath();
            return true;
        }
        catch (IOException e) {
            return false;
        }
    }
    
    public static KamiMod getInstance() {
        return KamiMod.INSTANCE;
    }
    
    public KamiGUI getGuiManager() {
        return this.guiManager;
    }
    
    public CommandManager getCommandManager() {
        return this.commandManager;
    }
    
    static {
        log = LogManager.getLogger("Zispanos Backdoor");
        EVENT_BUS = (EventBus)new EventManager();
    }
}
