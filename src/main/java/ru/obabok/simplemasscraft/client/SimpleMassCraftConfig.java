package ru.obabok.simplemasscraft.client;

import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;

@Config(name = "simplemasscraft")
public class SimpleMassCraftConfig implements ConfigData {

    public int massCraftDelay = 0;

    public static SimpleMassCraftConfig get() {
        return AutoConfig.getConfigHolder(SimpleMassCraftConfig.class).getConfig();
    }
}
