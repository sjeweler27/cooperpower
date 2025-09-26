package com.example.copperpower;

import net.fabricmc.api.ModInitializer;
public class CopperPowerMod implements ModInitializer {
    public static final String MOD_ID = "copperpower";

    @Override
    public void onInitialize() {
        System.out.println("CopperPower loaded - copper blocks now conduct redstone!");
    }
}
