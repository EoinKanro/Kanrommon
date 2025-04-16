package io.github.eoinkanro.mc.kanrommon;

import io.github.eoinkanro.mc.kanrommon.conf.Constants;
import net.minecraftforge.fml.common.Mod;

@Mod(Constants.MOD_ID)
public class Kanrommon {

    public Kanrommon() {
        Constants.LOG.info("Kanrommon is loaded :)");
    }

}