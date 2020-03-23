package org.inventivetalent.hypixel.fairysoulhelper;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.apache.logging.log4j.Logger;

@Mod(
    modid = FairySoulMod.MODID,
    name = FairySoulMod.NAME,
    version = FairySoulMod.VERSION
)
public class FairySoulMod {
    public static final String MODID = "fairysoulhelper";
    public static final String NAME = "Hypixel Skyblock Fairy Soul Helper";
    public static final String VERSION = "@VERSION@";
    public static final String MODVERSION = "@MODVERSION@";
    public static final String MCVERSION = "@MCVERSION@";

    public static Logger logger;

    public boolean updateAvailable = false;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        logger = event.getModLog();

        logger.info("Mod Version: " + VERSION);
        logger.info("Minecraft Version: " + MCVERSION);
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        logger.info("Hello World!");

        MinecraftForge.EVENT_BUS.register(new SpawnListener());
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) {}
}
