package sf.ssf.sfort;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;

import java.io.File;
import java.nio.file.Files;
import java.util.*;

public class Eats implements ModInitializer{
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
            String[] init =new String[Math.max(la.size(), defaultDesc.size() * 2)];
            Arrays.fill(init,"");
            String[] ls = la.toArray(init);
            for (int i = 0; i<defaultDesc.size();++i)
                ls[i*2+1]= defaultDesc.get(i);

            try{ delay =Math.max(Integer.parseInt(ls[0]),0);}catch (NumberFormatException ignored){}
            ls[0] = String.valueOf(delay);

            try{ difficultyScale = Math.max(Integer.parseInt(ls[2]),0);}catch (NumberFormatException ignored){}
            ls[2] = String.valueOf(difficultyScale);

            forceDelay = ls[4].contains("true");
            ls[4] = String.valueOf(forceDelay);


                for (int i = (defaultDesc.size()*2)+1; i<ls.length;i+=2){
                    ls[i] = "!#Unknown value / config from the future";
                }
            Files.write(confFile.toPath(), Arrays.asList(ls));
            System.out.println("tf.ssf.sfort.eternaleats successfully loaded config file");
        } catch(Exception e) {
            System.out.println("tf.ssf.sfort.eternaleats failed to load config file, using defaults\n"+e);
        }
    }
}
