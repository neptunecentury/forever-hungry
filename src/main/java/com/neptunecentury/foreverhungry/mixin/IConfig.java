package com.neptunecentury.foreverhungry.mixin;

public interface IConfig {
    /**
     * Sets defaults and returns whether any updates were made that need saved.
     *
     * @return True if updates were made that need saved
     */
    boolean setDefaults(boolean fromLoad);

}
