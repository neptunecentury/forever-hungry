package sf.ssf.sfort;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.nio.file.Files;
import java.util.*;

public class Eats implements ModInitializer{
    public static Logger LOGGER = LogManager.getLogger();

    public static int delay = 65;
    public static boolean forceDelay = false;
    public static int difficultyScale = 0;
    
    @Override
    public void onInitialize() {
        // Configs
        File confFile = new File(
                FabricLoader.getInstance().getConfigDir().toString(),
                "EternalEats.conf"
        );
        try {
            confFile.createNewFile();
            List<String> la = Files.readAllLines(confFile.toPath());
            List<String> defaultDesc = Arrays.asList(
                    "^-Duration in ticks between eating attempts when full [65]",
                    "^-By how much should cooldown scale with difficulty [0] // cooldown+(cooldown*difficulty*scale)",
                    "^-Force cooldown even if not full [false] true | false"
            );
            String[] ls = la.toArray(new String[Math.max(la.size(), defaultDesc.size() * 2)|1]);
            for (int i = 0; i<defaultDesc.size();++i)
                ls[i*2+1]= defaultDesc.get(i);
            try{ delay =Math.max(Integer.parseInt(ls[0]),0);}catch (Exception ignored){}
            ls[0] = String.valueOf(delay);

            try{ difficultyScale = Math.max(Integer.parseInt(ls[2]),0);}catch (Exception ignored){}
            ls[2] = String.valueOf(difficultyScale);

            try{forceDelay = ls[4].contains("true");}catch (Exception ignore){}
            ls[4] = String.valueOf(forceDelay);

            Files.write(confFile.toPath(), Arrays.asList(ls));
            LOGGER.log(Level.INFO,"tf.ssf.sfort.eternaleats successfully loaded config file");
        } catch(Exception e) {
            LOGGER.log(Level.ERROR,"tf.ssf.sfort.eternaleats failed to load config file, using defaults\n"+e);
        }
    }
}