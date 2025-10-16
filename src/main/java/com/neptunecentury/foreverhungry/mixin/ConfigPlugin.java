package com.neptunecentury.foreverhungry.mixin;

import org.objectweb.asm.tree.ClassNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

import java.util.*;

public class ConfigPlugin implements IMixinConfigPlugin {
    public static final String MOD_ID = "forever-hungry";
    public static final String mod = "com.neptunecentury.foreverhungry";
    public static final Logger logger = LoggerFactory.getLogger(MOD_ID);

    public static Config config;

    @Override
    public void onLoad(String mixinPackage) {
        // Create the config manager
        var configManager = new ConfigManager<Config>("forever-hungry", logger);
        // Load the config
        config = configManager.load(Config.class);

    }

    @Override
    public boolean shouldApplyMixin(String targetClassName, String mixinClassName) {
        if (Objects.equals(mixinClassName, mod + ".mixin.Instant")) {
            return !(config.foodTime == 32 && config.snackTime == 16);
        }

        return true;
    }

    @Override
    public String getRefMapperConfig() {
        return null;
    }

    @Override
    public void acceptTargets(Set<String> myTargets, Set<String> otherTargets) {
    }

    @Override
    public List<String> getMixins() {
        return null;
    }

    @Override
    public void preApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {
    }

    @Override
    public void postApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {
    }
}