package io.github.eoinkanro.mc.kanrommon;


import io.github.eoinkanro.mc.kanrommon.conf.Constants;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;

@Mod(Constants.MOD_ID)
public class Kanrommon {

    public Kanrommon(IEventBus eventBus) {
        Constants.LOG.info("Kanrommon is loaded :)");
    }

}