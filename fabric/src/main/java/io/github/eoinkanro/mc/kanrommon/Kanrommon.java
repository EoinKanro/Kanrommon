package io.github.eoinkanro.mc.kanrommon;

import io.github.eoinkanro.mc.kanrommon.conf.Constants;
import net.fabricmc.api.ModInitializer;

public class Kanrommon implements ModInitializer {
    
    @Override
    public void onInitialize() {
        Constants.LOG.info("Kanrommon is loaded :)");
    }

}
