package com.neptunecentury.foreverhungry.mixin;

public class Config implements IConfig {
    public int delay = 0;
    public boolean forceDelay = false;
    public int difficultyScale = 0;
    public int foodTime = 32;
    public int snackTime = 16;

    public Config() {

    }

    /**
     * Sets the default values
     */
    @Override
    public boolean setDefaults(boolean fromLoad) {
        if (fromLoad){
            return false;
        }

        delay = 0;
        forceDelay = false;
        difficultyScale = 0;
        foodTime = 32;
        snackTime = 16;

        // Return false in most cases.
        return false;

    }

}
