package tf.ssf.sfort.eternaleats.mixin;

import net.fabricmc.loader.api.FabricLoader;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;
import tf.ssf.sfort.ini.SFIni;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;

public class Config implements IMixinConfigPlugin {
    public static final String mod = "tf.ssf.sfort.eternaleats";
    public static Logger LOGGER = LogManager.getLogger();

    public static int delay = 0;
    public static boolean forceDelay = false;
    public static int difficultyScale = 0;
    public static int eatDurationX = 32, eatDurationY = 16;

    @Override
    public void onLoad(String mixinPackage) {
        SFIni defIni = new SFIni();
        defIni.load(String.join("\n", new String[]{
                "; Duration in ticks between eating attempts when full [0]",
                ";  Vanilla default: 65",
                "cooldown=0",
                "; By how much should cooldown scale with difficulty [0]",
                ";  cooldown+(cooldown*difficulty*scale)",
                "cooldownDifficultyScale=0",
                "; Force cooldown even if not full [false] true | false",
                "forceCooldown=false",
                "; Duration in ticks for eating normal food [32]",
                "foodTime=32",
                "; Duration in ticks for eating snack food [16]",
                "snackTime=16"
        }));
        loadLegacy(defIni);
        File confFile = new File(
                FabricLoader.getInstance().getConfigDir().toString(),
                "EternalEats.sf.ini"
        );
        if (!confFile.exists()) {
            try {
                Files.write(confFile.toPath(), defIni.toString().getBytes());
                LOGGER.log(Level.INFO,mod+" successfully created config file");
                loadIni(defIni);
            } catch (IOException e) {
                LOGGER.log(Level.ERROR,mod+" failed to create config file, using defaults", e);
            }
            return;
        }
        try {
            SFIni ini = new SFIni();
            String text = Files.readString(confFile.toPath());
            int hash = text.hashCode();
            ini.load(text);
            for (Map.Entry<String, List<SFIni.Data>> entry : defIni.data.entrySet()) {
                List<SFIni.Data> list = ini.data.get(entry.getKey());
                if (list == null || list.isEmpty()) {
                    ini.data.put(entry.getKey(), entry.getValue());
                } else {
                    list.get(0).comments = entry.getValue().get(0).comments;
                }
            }
            loadIni(ini);
            String iniStr = ini.toString();
            if (hash != iniStr.hashCode()) {
                Files.write(confFile.toPath(), iniStr.getBytes());
            }
        } catch (IOException e) {
            LOGGER.log(Level.ERROR,mod+" failed to load config file, using defaults", e);
        }
    }
    public static void loadIni(SFIni ini) {
        setOrResetInt(ini, "cooldown", d->delay=d, delay);
        setOrResetInt(ini, "cooldownDifficultyScale", d->difficultyScale=d, difficultyScale);
        setOrResetBool(ini, "forceCooldown", d->forceDelay=d, forceDelay);
        setOrResetInt(ini, "foodTime", d->eatDurationX=d, eatDurationX);
        setOrResetInt(ini, "snackTime", d->eatDurationY=d, eatDurationY);
        LOGGER.log(Level.INFO,mod+" finished loaded config file");
    }
    public static void setOrResetBool(SFIni ini, String key, Consumer<Boolean> set, boolean bool) {
        try {
            set.accept(ini.getBoolean(key));
        } catch (Exception e) {
            SFIni.Data data = ini.getLastData(key);
            if (data != null) data.val = Boolean.toString(bool);
            LOGGER.log(Level.ERROR,mod+" failed to load "+key+", setting to default value", e);
        }
    }
    public static void setOrResetInt(SFIni ini, String key, Consumer<Integer> set, int bool) {
        try {
            set.accept(ini.getInt(key));
        } catch (Exception e) {
            SFIni.Data data = ini.getLastData(key);
            if (data != null) data.val = Integer.toString(bool);
            LOGGER.log(Level.ERROR,mod+" failed to load "+key+", setting to default value", e);
        }
    }
    public static void loadLegacy(SFIni inIni) {
        Map<String, String> oldConf = new HashMap<>();
        File confFile = new File(
                FabricLoader.getInstance().getConfigDir().toString(),
                "EternalEats.conf"
        );
        try {
            List<String> la = Files.readAllLines(confFile.toPath());
            String[] ls = la.toArray(new String[Math.max(la.size(), 10)|1]);

            try{ oldConf.put("cooldown", Integer.toString(Math.max(Integer.parseInt(ls[0]),0)));}catch (Exception ignored){}
            try{ oldConf.put("cooldownDifficultyScale", Integer.toString(Math.max(Integer.parseInt(ls[2]),0)));}catch (Exception ignored){}
            try{ oldConf.put("forceCooldown", Boolean.toString(ls[4].contains("true")));}catch (Exception ignore){}
            try{ oldConf.put("foodTime", Integer.toString(Math.max(Integer.parseInt(ls[6]),1)));}catch (Exception ignored){}
            try{ oldConf.put("snackTime", Integer.toString(Math.max(Integer.parseInt(ls[8]),1)));}catch (Exception ignored){}

            for (Map.Entry<String, String> entry : oldConf.entrySet()) {
                SFIni.Data data = inIni.getLastData(entry.getKey());
                if (data != null) {
                    data.val = entry.getValue();
                }
            }

            Files.delete(confFile.toPath());
            LOGGER.log(Level.INFO,mod+" successfully loaded legacy config file");
        } catch(Exception e) {
            LOGGER.log(Level.ERROR,mod+" failed to load legacy config file, using defaults\n"+e);
        }
    }
    @Override
    public boolean shouldApplyMixin(String targetClassName, String mixinClassName) {
        switch (mixinClassName){
            case mod+".mixin.Instant":
                return !(eatDurationX == 32 && eatDurationY == 16);
            default:
                return true;
        }
    }
    @Override public String getRefMapperConfig() { return null; }
    @Override public void acceptTargets(Set<String> myTargets, Set<String> otherTargets) { }
    @Override public List<String> getMixins() { return null; }
    @Override public void preApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) { }
    @Override public void postApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) { }
}