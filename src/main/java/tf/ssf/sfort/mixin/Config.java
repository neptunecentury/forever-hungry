package tf.ssf.sfort.mixin;

import net.fabricmc.loader.api.FabricLoader;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

import java.awt.*;
import java.io.File;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

public class Config implements IMixinConfigPlugin {
    public static Logger LOGGER = LogManager.getLogger();

    public static int delay = 0;
    public static boolean forceDelay = false;
    public static int difficultyScale = 0;
    public static Point eatDuration = new Point(32, 16);

    @Override
    public void onLoad(String mixinPackage) {
        // Configs
        File confFile = new File(
                FabricLoader.getInstance().getConfigDir().toString(),
                "EternalEats.conf"
        );
        try {
            confFile.createNewFile();
            List<String> la = Files.readAllLines(confFile.toPath());
            List<String> defaultDesc = Arrays.asList(
                    "^-Duration in ticks between eating attempts when full [0] // Vanilla default: 65",
                    "^-By how much should cooldown scale with difficulty [0] // cooldown+(cooldown*difficulty*scale)",
                    "^-Force cooldown even if not full [false] true | false",
                    "^-Duration in ticks for eating normal food [32]",
                    "^-Duration in ticks for eating snack food [16]"
            );
            String[] ls = la.toArray(new String[Math.max(la.size(), defaultDesc.size() * 2)|1]);
            final int hash = Arrays.hashCode(ls);
            for (int i = 0; i<defaultDesc.size();++i)
                ls[i*2+1]= defaultDesc.get(i);
            try{ delay =Math.max(Integer.parseInt(ls[0]),0);}catch (Exception ignored){}
            ls[0] = String.valueOf(delay);

            try{ difficultyScale = Math.max(Integer.parseInt(ls[2]),0);}catch (Exception ignored){}
            ls[2] = String.valueOf(difficultyScale);

            try{forceDelay = ls[4].contains("true");}catch (Exception ignore){}
            ls[4] = String.valueOf(forceDelay);

            try{ eatDuration.x = Math.max(Integer.parseInt(ls[6]),1);}catch (Exception ignored){}
            ls[6] = String.valueOf(eatDuration.x);

            try{ eatDuration.y = Math.max(Integer.parseInt(ls[8]),1);}catch (Exception ignored){}
            ls[8] = String.valueOf(eatDuration.y);

            if (hash != Arrays.hashCode(ls))
                Files.write(confFile.toPath(), Arrays.asList(ls));
            LOGGER.log(Level.INFO,"tf.ssf.sfort.eternaleats successfully loaded config file");
        } catch(Exception e) {
            LOGGER.log(Level.ERROR,"tf.ssf.sfort.eternaleats failed to load config file, using defaults\n"+e);
        }
    }
    @Override
    public boolean shouldApplyMixin(String targetClassName, String mixinClassName) {
        switch (mixinClassName){
            case "tf.ssf.sfort.mixin.Eats":
                return true;
            case "tf.ssf.sfort.mixin.Instant":
                return !(eatDuration.x == 32 && eatDuration.y == 16);
            default:
                return false;
        }
    }
    @Override public String getRefMapperConfig() { return null; }
    @Override public void acceptTargets(Set<String> myTargets, Set<String> otherTargets) { }
    @Override public List<String> getMixins() { return null; }
    @Override public void preApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) { }
    @Override public void postApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) { }
}